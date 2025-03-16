package com.smart.framework.cache.guava;

import com.google.common.cache.Cache;
import com.smart.framework.cache.guava.data.CacheObject;
import com.smart.framework.commons.core.cache.CacheService;

import java.util.Set;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public interface GuavaCacheService extends CacheService {

    /**
     * 清理过期的
     */
    void clearExpire();

    /**
     * 获取所有缓存的key
     * @return 缓存key集合
     */
    Set<String> keys();

    /**
     * 获取缓存
     * @return 缓存
     */
    Cache<String, CacheObject<Object>> getCache();
}
