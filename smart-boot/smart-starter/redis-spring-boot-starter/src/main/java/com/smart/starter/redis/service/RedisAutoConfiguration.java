package com.smart.starter.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis配置类
 * @author shizhongming
 * 2020/1/17 8:45 下午
 */
@Configuration
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisService redisService(@Autowired RedisTemplate<Object, Object> redisTemplate) {
        return new RedisServiceImpl(redisTemplate);
    }
}
