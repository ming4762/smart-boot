package com.smart.commons.core.cache;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务
 * @author shizhongming
 * 2020/1/17 8:32 下午
 */
public interface CacheService {
    /**
     * 写入缓存
     * @param key 缓存的key
     * @param value 缓存的value
     */
    void put(@NonNull String key, @NonNull Object value);

    /**
     * 写入缓存，并设置有效时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param timeout 缓存的有效时间（单位：秒）
     */
    default void put(@NonNull String key, @NonNull Object value, long timeout) {
        put(key, value, Duration.ofSeconds(timeout));
    }

    /**
     * 写入缓存，并设置有效时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param timeout 缓存的有效时间
     */
    void put(@NonNull String key, @NonNull Object value, @NonNull Duration timeout);

    /**
     * 写入缓存，并设置过期时间
     * @param key 缓存的key
     * @param value 缓存的value
     * @param expireTime 缓存过期时间
     */
    void put(@NonNull String key, @NonNull Object value, @NonNull Instant expireTime);

    /**
     * 批量写入缓存
     * @param keyValues 缓存键值对
     */
    void batchPut(@NonNull Map<String, Object> keyValues);

    /**
     * 批量写入缓存,并设置有效时间
     * @param keyValues 缓存键值对
     * @param timeout 缓存有效时间（单位：秒）
     */
    void batchPut(@NonNull Map<String, Object> keyValues, long timeout);

    /**
     * 批量写入缓存,并设置有效时间
     * @param keyValues 缓存键值对
     * @param timeout 缓存有效时间
     */
    void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Duration timeout);

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    default void expire(@NonNull String key, long timeout) {
        this.expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置key的过期时间
     * @param key key
     * @param timeout 过期时间
     */
    void expire(@NonNull String key, Duration timeout);


    /**
     * 批量设置key的过期时间
     * @param keys key
     * @param timeout 过期时间
     */
    default void batchExpire(@NonNull Collection<String> keys, long timeout) {
        this.batchExpire(keys, Duration.ofSeconds(timeout));
    }

    /**
     * 批量设置key过期时间
     * @param keys key列表
     * @param timeout 过期时间
     */
    void batchExpire(@NonNull Collection<String> keys, Duration timeout);

    /**
     * 批量写入缓存,并设置过期时间
     * @param keyValues 缓存键值对
     * @param expireTime 缓存过期时间
     */
    void batchPut(@NonNull Map<String, Object> keyValues, @NonNull Instant expireTime);

    /**
     * 读取缓存
     * @param key 键
     * @param <T> 值类型
     * @return 值
    </T> */
    @Nullable
    <T> T get(@NonNull String key);


    /**
     * 批量量读取缓存
     * @param keys 键集合
     * @param <T> 值的类型
     * @return 值
    </T> */
    @Nullable
    <T> List<T> batchGet(@NonNull Collection<String> keys);

    /**
     * 删除缓存
     * @param key 键
     */
    void delete(@NonNull String key);

    /**
     * 批量删除
     * @param keys 键集合
     */
    void batchDelete(@NonNull List<String> keys);

    /**
     * 匹配key
     *
     * @param patternKey 所有key
     * @return 所有key
     */
    List<String> matchKeys(@NonNull String patternKey);

    /**
     * 是否存在换成
     * @param key key
     * @return 结果
     */
    boolean hasKey(@NonNull String key);

    /**
     * 匹配删除
     * @param prefixKey key匹配项
     */
    void matchDelete(@NonNull String prefixKey);

    /**
     * 获取并删除
     * @param key key
     * @return 数据
     */
    <T> T getAndRemove(@NonNull String key);
}
