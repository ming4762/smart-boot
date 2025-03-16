package com.smart.framework.auth.cache.guava.cache;

import com.smart.framework.auth.core.service.AbstractAuthCache;
import com.smart.framework.cache.guava.GuavaCacheService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * authCache Guava实现
 * @author shizhongming
 * TODO:未完成
 * 2020/9/11 9:40 下午
 */
public class GuavaAuthCache extends AbstractAuthCache<Object> {

    private final GuavaCacheService cacheService;


    public GuavaAuthCache(String prefix, GuavaCacheService cacheService) {
        super(prefix);
        this.cacheService = cacheService;
    }

    /**
     * 添加缓存
     *
     * @param key     key
     * @param mapKey  mapKey
     * @param value   value
     * @param timeout 超时时间
     */
    @Override
    public void put(String key, String mapKey, Object value, Duration timeout) {

    }

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
    public void putAll(String key, Map<String, Object> value, Duration timeout) {

    }

    @Override
    public void expire(@NonNull String key, Duration timeout) {
        this.cacheService.expire(this.getKey(key), timeout);
    }

    @Nullable
    @Override
    public Map<String, Object> get(@NonNull String key) {
        return this.cacheService.get(this.getKey(key));
    }

    /**
     * 获取缓存内容
     *
     * @param key key
     * @return value
     */
    @Override
    public Object getValue(String key) {
        return null;
    }

    /**
     * 获取缓存内容
     *
     * @param key    key
     * @param mapKey mapKey
     * @return value
     */
    @Override
    public Object get(String key, String mapKey) {
        return null;
    }

    @Override
    public void remove(@NonNull String key) {
        this.cacheService.delete(this.getKey(key));
    }

    @Override
    public Set<String> keys() {
        return this.cacheService.keys().stream()
                .map(this::getRealKey)
                .collect(Collectors.toSet());
    }


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @Override
    @NonNull
    public List<Map<String, Object>> batchGet(@NonNull Collection<String> keys) {
        var prefixKeys = keys.stream().map(this::getKey).collect(Collectors.toSet());
        List<Map<String, Object>> cacheList = this.cacheService.batchGet(new ArrayList<>(prefixKeys));
        return cacheList == null ? Collections. emptyList() : cacheList;
    }

    @Override
    public void matchRemove(@NonNull String matchKey) {
        this.cacheService.matchDelete(this.getKey(matchKey));
    }

    @Override
    public List<Map<String, Object>> matchGet(@NonNull String matchKey) {
        List<String> keys = this.cacheService.matchKeys(this.getKey(matchKey));
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> dataList = this.cacheService.batchGet(keys);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections. emptyList();
        }
        return dataList;
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
        return this.cacheService.getAndRemove(key);
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
    public boolean hasKey(String key) {
        return false;
    }
}
