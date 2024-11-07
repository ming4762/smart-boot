package com.smart.framework.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smart.framework.cache.guava.data.CacheObject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务类-基于GUAVA实现
 * @author ShiZhongMing
 * @since 1.0
 */
@Slf4j
public class GuavaCacheServiceImpl implements GuavaCacheService {

    private final Cache<String, CacheObject<Object>> cache;

    public GuavaCacheServiceImpl() {
        this.cache = CacheBuilder.newBuilder()
                .build();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = this.createThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();
        threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            try {
                log.debug("start clear guava cache");
                long startTime = System.nanoTime();
                this.clearExpire();
                log.debug("clear up complete, use[{}]ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, Duration.ofMillis(30L * 1000L));
    }

    private ThreadPoolTaskScheduler createThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(false);
        taskScheduler.setThreadNamePrefix("guava-cache");
        return taskScheduler;
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value) {
        CacheObject<Object> cacheObject = new CacheObject<>(Instant.now(), value, null);
        this.cache.put(key, cacheObject);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, long timeout) {
        this.cache.put(key, new CacheObject<>(value, Duration.ofSeconds(timeout)));
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Duration timeout) {
        this.cache.put(key, new CacheObject<>(value, timeout));
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Instant expireTime) {
        this.cache.put(key, new CacheObject<>(value, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli())));
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues) {
        Map<String, CacheObject<Object>> cacheObjectMap = HashMap.newHashMap(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(key, new CacheObject<>(value, null)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, long timeout) {
        this.batchPut(keyValues, Duration.ofSeconds(timeout));
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Duration timeout) {
        Map<String, CacheObject<Object>> cacheObjectMap = HashMap.newHashMap(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(key, new CacheObject<>(value, timeout)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Instant expireTime) {
        this.batchPut(keyValues, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli()));
    }

    @Override
    public void expire(@NonNull String key, Duration timeout) {
        CacheObject<Object> cacheObject = this.cache.getIfPresent(key);
        if (cacheObject != null) {
            cacheObject.setTimeout(timeout);
            cacheObject.setOperationTime(Instant.now());
            this.cache.put(key, cacheObject);
        }
    }

    /**
     * 批量设置key过期时间
     *
     * @param keys    key列表
     * @param timeout 过期时间
     */
    @Override
    public void batchExpire(@org.springframework.lang.NonNull Collection<String> keys, Duration timeout) {
        keys.forEach(item -> this.expire(item, timeout));
    }

    @Nullable
    @Override
    public <T> T get(@NonNull String key) {
        CacheObject<Object> cacheObject = this.cache.getIfPresent(key);
        if (cacheObject == null) {
            return null;
        }
        // 判断是否超时
        if (cacheObject.getTimeout() != null && cacheObject.getOperationTime().plus(cacheObject.getTimeout()).isBefore(Instant.now())) {
            // 超时删除
            this.cache.invalidate(key);
            return null;
        }
        return (T) cacheObject.getData();
    }

    @Nullable
    @Override
    public <T> List<T> batchGet(@NonNull Collection<String> keys) {
        return keys.stream().map(this :: <T>get).toList();
    }


    @Override
    public void delete(@NonNull String key) {
        this.cache.invalidate(key);
    }

    @Override
    public void batchDelete(@NonNull List<String> keys) {
        this.cache.invalidateAll(keys);
    }

    @Override
    public List<String> matchKeys(@NonNull String patternKey) {
        this.clearExpire();
        return this.cache.asMap().keySet().stream()
                .filter(objectCacheObject -> objectCacheObject.startsWith(patternKey))
                .toList();
    }

    @Override
    public boolean hasKey(@NonNull String key) {
        return this.get(key) != null;
    }

    @Override
    public void matchDelete(@NonNull String prefixKey) {
        List<String> keys = this.matchKeys(prefixKey);
        if (CollectionUtils.isEmpty(keys)) {
            this.batchDelete(keys);
        }
    }

    @Override
    public synchronized void clearExpire() {
        this.cache.asMap().forEach((key, value) -> {
            if (value.getTimeout() != null && value.getOperationTime().plus(value.getTimeout()).isBefore(Instant.now())) {
                this.delete(key);
            }
        });
    }

    @Override
    public Set<String> keys() {
        return this.cache.asMap().keySet();
    }


    /**
     * 获取并删除
     *
     * @param key key
     * @return 数据
     */
    @Override
    public synchronized Object getAndRemove(@org.springframework.lang.NonNull String key) {
        Object data = this.get(key);
        if (data != null) {
            this.delete(key);
        }
        return data;
    }
}
