package com.smart.framework.redis.service;

import com.smart.framework.commons.core.lock.limit.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author ShiZhongMing
 * 2022/8/26 9:34
 * @since 1.0 connection.eval(LUA_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.MULTI, )
 */
@RequiredArgsConstructor
public class RedisRateLimitServiceImpl implements RateLimitService {

    private static final String RATE_LIMIT_CONFIG_KEY = "smart_rate_limit_config";
    private static final String RATE_LIMIT_LOCK_KEY = "lock:rateLimiter:";

    private final RedisService redisService;

    @Override
    public boolean acquire(@NonNull String key, long limit, @NonNull ChronoUnit unit) {
        RRateLimiter rateLimiter = this.redisService.getRateLimiter(key);

        // 加锁，避免并发删除
        Lock lock = this.redisService.getLock(RATE_LIMIT_LOCK_KEY + key);
        lock.lock();
        try {
            if (this.isConfigChanged(key, limit, unit)) {
                // 限流器发生变更删除限流器
                rateLimiter.delete();
            }
            if (!rateLimiter.isExists()) {
                boolean success = rateLimiter.trySetRate(RateType.OVERALL, limit, Duration.of(1L, unit));
                if (!success) {
                    throw new IllegalStateException("Failed to set rate limiter for key: " + key);
                }
                // 将配置放入缓存
                this.redisService.hashPut(RATE_LIMIT_CONFIG_KEY, key, new RateLimitConfig(limit, unit.name()));
            }
        } finally {
            lock.unlock();
        }
        return rateLimiter.tryAcquire();
    }

    /**
     * 限流器配置是否发生变更
     * @param key 限流器key
     * @param limit 每秒访问次数
     * @param unit 时间单位
     * @return 是否发生变更
     */
    private boolean isConfigChanged(String key, long limit, ChronoUnit unit) {
        RateLimitConfig config = this.redisService.hashGet(RATE_LIMIT_CONFIG_KEY, key);
        if (config == null) {
            return false;
        }
        return config.limit() != limit || !config.unitName().equals(unit.name());
    }

    private record RateLimitConfig(long limit, String unitName) {
    }
}
