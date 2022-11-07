package com.smart.auth.cache.guava.cache;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AbstractAuthCache;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

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
        this.cacheService.put(this.getKey(key), value, timeout);
    }

    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.cacheService.expire(this.getKey(key), timeout);
    }

    @Override
    public Object get(@NonNull String key) {
        return this.cacheService.get(this.getKey(key));
    }

    @Override
    public void remove(@NonNull String key) {
        this.cacheService.delete(this.getKey(key));
    }

    @Override
    public Set<String> keys() {
        return this.cacheService.keys().stream()
                .map(item -> this.getRealKey(item.toString()))
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
        Set<String> prefixKeys = keys.stream().map(this::getKey).collect(Collectors.toSet());
        List<Object> cacheList = this.cacheService.batchGet(new ArrayList<>(prefixKeys));
        return cacheList == null ? new HashSet<>(0) : new HashSet<>(cacheList);
    }

    @Override
    public void matchRemove(@NonNull String matchKey) {
        this.cacheService.matchDelete(this.getKey(matchKey));
    }

    @Override
    public Set<Object> matchGet(@NonNull String matchKey) {
        List<Object> keys = this.cacheService.matchKeys(this.getKey(matchKey));
        if (CollectionUtils.isEmpty(keys)) {
            return new HashSet<>(0);
        }
        List<Object> dataList = this.cacheService.batchGet(keys);
        if (CollectionUtils.isEmpty(dataList)) {
            return new HashSet<>(0);
        }
        return new HashSet<>(dataList);
    }

    @Override
    public Set<String> matchKeys(@NonNull String matchKey) {
        return this.cacheService.matchKeys(this.getKey(matchKey))
                .stream()
                .map(item -> this.getRealKey(item.toString()))
                .collect(Collectors.toSet());
    }
}
