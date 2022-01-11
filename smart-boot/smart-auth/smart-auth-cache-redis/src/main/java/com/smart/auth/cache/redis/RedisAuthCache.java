package com.smart.auth.cache.redis;

import com.smart.auth.core.service.AbstractAuthCache;
import com.smart.starter.redis.service.RedisService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/7/1 3:54 下午
 */
public class RedisAuthCache extends AbstractAuthCache<String, Object> {

    private final RedisService cacheService;

    public RedisAuthCache(RedisService cacheService, String prefix) {
        super(prefix);
        this.cacheService = cacheService;
    }
    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    @Override
    public void put(@NonNull String key, @NonNull Object value, Duration timeout) {
        this.cacheService.put(this.getKey(key), value, timeout);
    }

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.cacheService.expire(this.getKey(key), timeout);
    }

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Override
    @Nullable
    public Object get(@NonNull String key) {
        return this.cacheService.get(this.getKey(key));
    }

    /**
     * 删除缓存
     * @param key key
     */
    @Override
    public void remove(@NonNull String key) {
        this.cacheService.delete(this.getKey(key));
    }

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    @Override
    public Set<String> keys() {
        // TODO: 待开发
        return new HashSet<>(0);
    }


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public Set<Object> batchGet(@NonNull Collection<String> keys) {
        final Set<String> prefixKeys = keys.stream().map(this::getKey).collect(Collectors.toSet());
        List<Object> data = this.cacheService.batchGet(Collections.singleton(prefixKeys));
        return Objects.isNull(data) ? new HashSet<>(0) : new HashSet<>(data);
    }
}
