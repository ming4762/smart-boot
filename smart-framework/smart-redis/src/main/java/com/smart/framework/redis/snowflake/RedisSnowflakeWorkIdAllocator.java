package com.smart.framework.redis.snowflake;

import com.smart.framework.commons.core.exception.SystemException;
import com.smart.framework.commons.core.utils.snowflake.AbstractSnowflakeWorkIdAllocator;
import com.smart.framework.redis.service.RedisService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis实现类雪花ID工作ID分配器
 * @author shizhongming
 * 2025/9/30 16:33
 * @since 5.0.0
 */
@Slf4j
public class RedisSnowflakeWorkIdAllocator extends AbstractSnowflakeWorkIdAllocator {

    private static final String WORK_ID_PREFIX = "snowflake:workid";
    private static final Duration WORK_ID_EXPIRE = Duration.ofSeconds(30);

    /**
     * 工作空间，用于不同项目共用redis的情况
     */
    private final String workspace;
    private final RedisService redisService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final ReentrantLock lock = new ReentrantLock();

    private long workerId;
    private boolean shutdown = false;
    private String redisValue;
    private volatile boolean initialized = false;

    public RedisSnowflakeWorkIdAllocator(String workspace, RedisService redisService) {
        this.workspace = workspace;
        this.redisService = redisService;
        this.threadPoolTaskScheduler = this.createThreadPoolTaskScheduler();
    }

    public void init() {
        this.threadPoolTaskScheduler.initialize();
        this.acquireWorkId();
        // 每10秒续期一次WORK ID
        // 初始延迟10秒
        Duration duration = Duration.ofSeconds(10);
        this.threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            lock.lock();
            try {
                if (shutdown) {
                    return;
                }
                // 重新续期WORK ID
                this.renew();
            } finally {
                lock.unlock();
            }
        }, Instant.now().plus(duration), duration);
        this.initialized = true;
    }

    /**
     * 分配WORK ID
     *
     * @return WORK ID
     */
    @Override
    public long allocateWorkId() {
        if (!this.initialized) {
            synchronized (this) {
                if (!this.initialized) {
                    this.init();
                }
            }
        }
        return this.workerId;
    }

    /**
     * 尝试获取WORK ID
     */
    protected void acquireWorkId() {
        lock.lock();
        try {
            SecureRandom random = new SecureRandom();
            long randomWorkId = random.nextLong(this.workerIdMax) & Long.MAX_VALUE % this.workerIdMax;
            String uuid = UUID.randomUUID().toString();
            boolean success = false;
            for (long i = 0; i < this.workerIdMax; i++) {
                // 尝试获取WORK ID
                RBucket<String> bucket = this.getBucket(randomWorkId);
                success = bucket.setIfAbsent(uuid, WORK_ID_EXPIRE);
                if (success) {
                    this.workerId = randomWorkId;
                    this.redisValue = uuid;
                    log.info("🎉 Acquired workId={} for instanceId={}", randomWorkId, uuid);
                    break;
                }
                randomWorkId = (randomWorkId + 1) % this.workerIdMax;
            }
            if (!success) {
                throw new SystemException("No available workId in range 0~" + this.workerIdMax);
            }
        } finally {
            lock.unlock();
        }
    }

    private void renew() {
        RBucket<String> bucket = this.getBucket(this.workerId);
        String value = bucket.get();
        if (redisValue == null || !redisValue.equals(value)) {
            // WORK ID发生冲突，重新申请
            log.warn("workId {} is not allocated by instanceId {}", this.workerId, value);
            this.acquireWorkId();
            return;
        }
        // WORK ID未过期，续期
        bucket.expire(WORK_ID_EXPIRE);
        log.debug("Renewed workId {} for instanceId {}", this.workerId, redisValue);
    }

    @PreDestroy
    public void destroy() {
        lock.lock();
        try {
            shutdown = true;
            redisValue = null;
        } finally {
            lock.unlock();
        }
        // 延迟30秒过期，确保其他实例可以获取到WORK ID
        this.getBucket(this.workerId).expire(Duration.ofSeconds(30));
        log.info("Released workId {}", this.workerId);
        this.threadPoolTaskScheduler.destroy();
    }

    /**
     * 获取WORK ID对应的Redis Bucket
     * @param workerId WORK ID
     * @return Redis Bucket
     */
    private RBucket<String> getBucket(long workerId) {
        return this.redisService.getRedissonClient().getBucket(WORK_ID_PREFIX + ":" + workspace + ":" + workerId);
    }

    private ThreadPoolTaskScheduler createThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(false);
        taskScheduler.setThreadNamePrefix("redis-snowflake-workid-allocator");
        return taskScheduler;
    }
}
