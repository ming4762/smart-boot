package com.smart.framework.auth.core.service;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 认证缓存管理器
 * @author shizhongming
 * 2020/7/1 1:59 下午
 */
public interface AuthCache<V> {

    /**
     * 添加缓存
     * @param key key
     * @param mapKey mapKey
     * @param value value
     * @param timeout 超时时间
     */
    void put(@NonNull String key, @NonNull String mapKey, @NonNull V value, Duration timeout);

    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    void put(@NonNull String key, @NonNull V value, Duration timeout);

    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    void putAll(@NonNull String key, @NonNull Map<String, V> value, Duration timeout);

    /**
     * 设置超时时间
     * @param key key
     * @param timeout 超时时间
     */
    void expire(@NonNull String key, Duration timeout);

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    @Nullable
    Map<String, V> get(@NonNull String key);

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    V getValue(@NonNull String key);

    /**
     * 获取缓存内容
     * @param key key
     * @param mapKey mapKey
     * @return value
     */
    @Nullable
    V get(@NonNull String key, @NonNull String mapKey);

    /**
     * 删除缓存
     * @param key key
     */
    void remove(@NonNull String key);

    /**
     * 获取所有key的集合
     * @return key的集合
     */
    Set<String> keys();


    /**
     * 批量获取
     * @param keys keys
     * @return 获取的缓存
     */
    @NonNull
    List<Map<String, V>> batchGet(@NonNull Collection<String> keys);

    /**
     * 匹配删除
     * @param matchKey 匹配的key
     */
    void matchRemove(@NonNull String matchKey);

    /**
     * 匹配获取
     * @param matchKey 匹配的key
     * @return 匹配结果
     */
    List<Map<String, V>> matchGet(@NonNull String matchKey);

    /**
     * 匹配获取key
     * @param matchKey 匹配的key
     * @return 匹配结果
     */
    Set<String> matchKeys(@NonNull String matchKey);

    /**
     * 获取并删除
     * @param key key
     * @return 数据
     */
    Map<String, V> getAndRemove(@NonNull String key);

    /**
     * 重命名
     * @param oldKey 旧key
     * @param newKey 新key
     */
    void rename(@NonNull String oldKey, @NonNull String newKey);

    /**
     * 是否存在key
     * @param key key
     * @return 是否存在
     */
    boolean hasKey(@NonNull String key);
}
