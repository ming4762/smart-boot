package com.smart.starter.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smart.starter.cache.guava.data.CacheObject;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Slf4j
public class GuavaCacheServiceImpl implements GuavaCacheService {

    private final Cache<Object, CacheObject<Object>> cache;

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
        taskScheduler.setThreadNamePrefix("guava cache clear");
        return taskScheduler;
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value) {
        CacheObject<Object> cacheObject = new CacheObject<>(Instant.now(), value, null);
        this.cache.put(key, cacheObject);
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, long timeout) {
        this.cache.put(key, new CacheObject<>(value, Duration.ofSeconds(timeout)));
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, @NonNull Duration timeout) {
        this.cache.put(key, new CacheObject<>(value, timeout));
    }

    @Override
    public void put(@NonNull Object key, @NonNull Object value, @NonNull Instant expireTime) {
        this.cache.put(key, new CacheObject<>(value, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli())));
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues) {
        Map<Object, CacheObject<Object>> cacheObjectMap = new HashMap<>(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(key, new CacheObject<>(value, null)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, long timeout) {
        this.batchPut(keyValues, Duration.ofSeconds(timeout));
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Duration timeout) {
        Map<Object, CacheObject<Object>> cacheObjectMap = new HashMap<>(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(key, new CacheObject<>(value, timeout)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<Object, Object> keyValues, @NonNull Instant expireTime) {
        this.batchPut(keyValues, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli()));
    }

    @Override
    public void expire(@NonNull Object key, long timeout) {
        this.expire(key, Duration.ofSeconds(timeout));
    }

    @Override
    public void expire(@NonNull Object key, Duration timeout) {
        CacheObject<Object> cacheObject = this.cache.getIfPresent(key);
        if (cacheObject != null) {
            cacheObject.setTimeout(timeout);
            cacheObject.setOperationTime(Instant.now());
            this.cache.put(key, cacheObject);
        }
    }

    @Override
    public void batchExpire(@NonNull Collection<Object> keys, long timeout) {
        keys.forEach(item -> this.expire(item, timeout));
    }


    @Nullable
    @Override
    public Object get(@NonNull Object key) {
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
        return cacheObject.getData();
    }

    @Nullable
    @Override
    public <T> T get(@NonNull Object key, @NonNull Class<T> clazz) {
        return (T)this.get(key);
    }

    @Nullable
    @Override
    public List<Object> batchGet(@NonNull Collection<Object> keys) {
        return keys.stream().map(this :: get).collect(Collectors.toList());
    }

    @Nullable
    @Override
    public <T> List<T> batchGet(@NonNull Collection<Object> keys, @NonNull Class<T> clazz) {
        return keys.stream().map(key -> (T)this.get(key)).collect(Collectors.toList());
    }

    @Override
    public void delete(@NonNull Object key) {
        this.cache.invalidate(key);
    }

    @Override
    public void batchDelete(@NonNull List<Object> keys) {
        this.cache.invalidateAll(keys);
    }

    @Override
    public List<Object> matchKeys(@NonNull Object patternKey) {
        this.clearExpire();
        return this.cache.asMap().keySet().stream()
                .filter(objectCacheObject -> objectCacheObject.toString().startsWith(patternKey.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasKey(@NonNull Object key) {
        return this.get(key) != null;
    }

    @Override
    public void matchDelete(@NonNull Object prefixKey) {
        List<Object> keys = this.matchKeys(prefixKey);
        if (CollectionUtils.isNotEmpty(keys)) {
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
    public Set<Object> keys() {
        return this.cache.asMap().keySet();
    }

}
