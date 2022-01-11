package com.smart.starter.redis.service;

import com.smart.commons.core.cache.CacheService;
import org.springframework.lang.NonNull;

/**
 * redis服务层
 * @author zhongming
 */
public interface RedisService extends CacheService {

    /**
     * 匹配删除
     * @param prefixKey key匹配项
     */
    void matchDelete(@NonNull Object prefixKey);

}
