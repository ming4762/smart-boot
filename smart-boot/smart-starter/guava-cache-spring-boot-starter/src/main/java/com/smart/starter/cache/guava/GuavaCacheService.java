package com.smart.starter.cache.guava;

import com.smart.commons.core.cache.CacheService;

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
    Set<Object> keys();
}
