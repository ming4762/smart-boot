package com.smart.framework.auth.cache.redis;


import com.smart.framework.auth.core.service.AbstractAuthCache;
import com.smart.framework.redis.service.RedisService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/7/1 3:54 下午
 */
public class RedisAuthCache extends AbstractAuthCache<Object> {

    private final RedisService cacheService;

    public RedisAuthCache(RedisService cacheService, String prefix) {
        super(prefix);
        this.cacheService = cacheService;
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param mapKey  mapKey
     * @param value   value
     */
    @Override
    public void putMap(@NonNull String key, @NonNull String mapKey, @NonNull Object value) {
        this.cacheService.hashPut(this.getKey(key), mapKey, value);
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void put(@NonNull String key, @NonNull Object value, Duration timeout) {
        this.cacheService.put(this.getKey(key), value, timeout);
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void putAll(@NonNull String key, @NonNull Map<String, Object> value, Duration timeout) {
        this.cacheService.hashPutAll(this.getKey(key), value);
        this.cacheService.expire(this.getKey(key), timeout);
    }

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.cacheService.hashExpire(this.getKey(key), timeout);
    }

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Override
    @Nullable
    public Map<String, Object> get(@NonNull String key) {
        return this.cacheService.hashEntries(this.getKey(key));
    }

    /**
     * 获取缓存内容
     *
     * @param key key
     * @return value
     */
    @Override
    public Object getValue(@NonNull String key) {
        return this.cacheService.get(this.getKey(key));
    }

    /**
     * 获取缓存内容
     *
     * @param key    key
     * @param mapKey mapKey
     * @return value
     */
    @Override
    public Object get(@NonNull String key, @NonNull String mapKey) {
        return this.cacheService.hashGet(this.getKey(key), mapKey);
    }

    /**
     * 删除缓存
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        this.cacheService.hashDelete(this.getKey(key));
    }

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    @Override
    public Set<String> keys() {
        return this.matchKeys("");
    }


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public List<Map<String, Object>> batchGet(@NonNull Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return keys.stream()
                .map(item -> this.cacheService.<String, Object>hashEntries(this.getKey(item)))
                .toList();
    }

    @Override
    public void matchRemove(@NonNull String matchKey) {
        List<String> keys = this.cacheService.matchKeys(getKey(matchKey));
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        keys.forEach(this.cacheService::hashDelete);
    }

    @Override
    public List<Map<String, Object>> matchGet(@NonNull String matchKey) {
        List<String> keys = this.cacheService.matchKeys(this.getKey(matchKey));
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return keys.stream()
                .map(item -> this.cacheService.<String, Object>hashEntries(this.getKey(item)))
                .toList();
    }

    @Override
    public Set<String> matchKeys(@NonNull String matchKey) {
        return this.cacheService.matchKeys(this.getKey(matchKey))
                .stream()
                .map(this::getRealKey)
                .collect(Collectors.toSet());
    }

    /**
     * 获取并删除
     *
     * @param key key
     * @return 数据
     */
    @Override
    public Map<String, Object> getAndRemove(@NonNull String key) {
        Map<String, Object> data = this.cacheService.hashEntries(this.getKey(key));
        this.cacheService.hashDelete(this.getKey(key));
        return data;
    }

    /**
     * 重命名
     *
     * @param oldKey 旧key
     * @param newKey 新key
     */
    @Override
    public void rename(@NonNull String oldKey, @NonNull String newKey) {
        this.cacheService.rename(this.getKey(oldKey), this.getKey(newKey));
    }

    /**
     * 是否存在key
     *
     * @param key key
     * @return 是否存在
     */
    @Override
    public boolean hasKey(@NonNull String key) {
        return this.cacheService.hasKey(this.getKey(key));
    }
}
