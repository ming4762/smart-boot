package com.smart.auth.cache.guava.cache;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AbstractAuthCache;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * authCache Guava实现
 * @author shizhongming
 * 2020/9/11 9:40 下午
 */
public class GuavaAuthCache extends AbstractAuthCache<String, Object> {

    private final GuavaCacheService cacheService;


    public GuavaAuthCache(AuthProperties authProperties, GuavaCacheService cacheService) {
        super(authProperties.getPrefix());
        this.cacheService = cacheService;
    }

    @Override
    public void put(@NonNull String key, @NonNull Object value, Duration timeout) {
        this.cacheService.put(key, value, timeout);
    }

    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.cacheService.expire(key, timeout);
    }

    @Override
    public Object get(@NonNull String key) {
        return this.cacheService.get(key);
    }

    @Override
    public void remove(@NonNull String key) {
        this.cacheService.delete(key);
    }

    @Override
    public Set<String> keys() {
        return this.cacheService.keys()
                .stream().map(Object::toString)
                .collect(Collectors.toSet());
    }


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public Set<Object> batchGet(@NonNull Collection<String> keys) {
        List<Object> cacheList = this.cacheService.batchGet(Collections.singleton(keys));
        return cacheList == null ? new HashSet<>(0) : new HashSet<>(cacheList);
    }
}
