package com.smart.starter.redis.service;

import com.smart.commons.core.cache.CacheService;
import com.smart.starter.redis.constants.RedisInfoParameterEnum;
import com.smart.starter.redis.model.RedisKeySpace;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Properties;

/**
 * redis服务层
 * @author zhongming
 */
public interface RedisService extends CacheService {

    /**
     * 查询redis info信息
     * @param parameter 参数
     * @return redis info信息
     */
    Properties info(@Nullable RedisInfoParameterEnum parameter);

    /**
     * 查询redis key space数据
     * @return redis key space数据
     */
    @NonNull
    Map<String, RedisKeySpace> queryKeySpace();
}
