package com.smart.framework.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.smart.framework.cache.guava.data.CacheObject;
import com.smart.framework.commons.core.cache.AbstractCacheService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 缓存服务类-基于GUAVA实现
 * @author ShiZhongMing
 * // TODO:带完善：缓存淘汰策略
 * @since 1.0
 */
@Slf4j
public class GuavaCacheServiceImpl extends AbstractCacheService implements GuavaCacheService {

    private final Cache<String, CacheObject<Object>> cache;

    public GuavaCacheServiceImpl(String keyPrefix) {
        super(keyPrefix);
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
        this.cache.put(this.getCachedKey(key), cacheObject);
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, long timeout) {
        this.cache.put(this.getCachedKey(key), new CacheObject<>(value, Duration.ofSeconds(timeout)));
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Duration timeout) {
        this.cache.put(this.getCachedKey(key), new CacheObject<>(value, timeout));
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, @NonNull Instant expireTime) {
        this.cache.put(this.getCachedKey(key), new CacheObject<>(value, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli())));
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues) {
        Map<String, CacheObject<Object>> cacheObjectMap = HashMap.newHashMap(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(this.getCachedKey(key), new CacheObject<>(value, null)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, long timeout) {
        this.batchPut(keyValues, Duration.ofSeconds(timeout));
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Duration timeout) {
        Map<String, CacheObject<Object>> cacheObjectMap = HashMap.newHashMap(keyValues.size());
        keyValues.forEach((key, value) -> cacheObjectMap.put(this.getCachedKey(key), new CacheObject<>(value, timeout)));
        this.cache.putAll(cacheObjectMap);
    }

    @Override
    public void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Instant expireTime) {
        this.batchPut(keyValues, Duration.ofMillis(expireTime.toEpochMilli() - Instant.now().toEpochMilli()));
    }

    @Override
    public void expire(@NonNull String key, Duration timeout) {
        CacheObject<Object> cacheObject = this.cache.getIfPresent(this.getCachedKey(key));
        if (cacheObject != null) {
            cacheObject.setTimeout(timeout);
            cacheObject.setOperationTime(Instant.now());
            this.cache.put(this.getCachedKey(key), cacheObject);
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
        keys.forEach(item -> this.expire(this.getCachedKey(item), timeout));
    }

    @Nullable
    @Override
    public <T> T get(@NonNull String key) {
        CacheObject<Object> cacheObject = this.cache.getIfPresent(this.getCachedKey(key));
        if (cacheObject == null) {
            return null;
        }
        // 判断是否超时
        if (cacheObject.getTimeout() != null && cacheObject.getOperationTime().plus(cacheObject.getTimeout()).isBefore(Instant.now())) {
            // 超时删除
            this.cache.invalidate(this.getCachedKey(key));
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
        this.cache.invalidate(this.getCachedKey(key));
    }

    @Override
    public void batchDelete(@NonNull List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        List<String> prefixKeys = keys.stream().map(this::getCachedKey)
                .toList();
        this.cache.invalidateAll(prefixKeys);
    }

    @Override
    public List<String> matchKeys(@NonNull String patternKey) {
        this.clearExpire();
        int subLength = this.getKeyPrefix().length();
        return this.cache.asMap().keySet().stream()
                .filter(objectCacheObject -> objectCacheObject.startsWith(this.getCachedKey(patternKey)))
                .map(item -> item.substring(subLength))
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
        int subLength = this.getKeyPrefix().length();
        return this.cache.asMap().keySet().stream()
                .map(item -> item.substring(subLength))
                .collect(Collectors.toSet());
    }

    /**
     * 获取缓存
     *
     * @return 缓存
     */
    @Override
    public Cache<String, CacheObject<Object>> getCache() {
        return this.cache;
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

    /**
     * 重命名缓存
     *
     * @param oldKey 旧key
     * @param newKey 新key
     */
    @Override
    public void rename(String oldKey, String newKey) {
        String oldCachedKey = this.getCachedKey(oldKey);
        String newCachedKey = this.getCachedKey(newKey);
        CacheObject<Object> value = this.cache.getIfPresent(oldCachedKey);
        if (value != null) {
            this.cache.put(newCachedKey, value);
            this.cache.invalidate(oldCachedKey);
        }
    }

    /**
     * 获取缓存过期时间
     *
     * @param key key
     * @return 过期时间，null代表无限大
     */
    @Override
    public Duration getExpire(@NonNull String key) {
        String cachedKey = this.getCachedKey(key);
        CacheObject<Object> cacheObject = this.cache.getIfPresent(cachedKey);
        if (cacheObject == null) {
            return Duration.ZERO;
        }
        if (cacheObject.getTimeout() == null) {
            return null;
        }
        Instant expire = cacheObject.getOperationTime().plus(cacheObject.getTimeout());
        Instant now = Instant.now();
        if (expire.isBefore(now)) {
            // 已过期，但是还未清理
            return Duration.ZERO;
        }
        return Duration.between(now, expire);
    }
}
