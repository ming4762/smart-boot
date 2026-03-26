package com.smart.framework.auth.core.service;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
public interface AuthCache {

    /**
     * 添加缓存
     * @param key key
     * @param mapKey mapKey
     * @param value value
     */
    void putMap(@NonNull String key, @NonNull String mapKey, @NonNull Object value);

    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    void put(@NonNull String key, @NonNull Object value, Duration timeout);

    /**
     * 添加缓存
     * @param key key
     * @param value value
     * @param timeout 超时时间
     */
    void putAll(@NonNull String key, @NonNull Map<String, Object> value, Duration timeout);

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
    <R> Map<String, R> get(@NonNull String key);

    /**
     * 获取缓存内容
     * @param key key
     * @return value
     */
    <R> R getValue(@NonNull String key);

    /**
     * 获取缓存内容
     * @param key key
     * @param mapKey mapKey
     * @return value
     */
    @Nullable
    <R> R get(@NonNull String key, @NonNull String mapKey);

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
    <R> List<Map<String, R>> batchGet(@NonNull Collection<String> keys);

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
    <R> List<Map<String, R>> matchGet(@NonNull String matchKey);

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
    <R> Map<String, R> getAndRemove(@NonNull String key);

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
