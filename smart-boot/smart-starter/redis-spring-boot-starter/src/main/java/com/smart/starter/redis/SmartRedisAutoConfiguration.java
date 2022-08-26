package com.smart.starter.redis;

import com.smart.commons.core.lock.limit.RateLimitService;
import com.smart.starter.redis.service.RedisRateLimitServiceImpl;
import com.smart.starter.redis.service.RedisService;
import com.smart.starter.redis.service.RedisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis配置类
 * @author shizhongming
 * 2020/1/17 8:45 下午
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class SmartRedisAutoConfiguration {

    @Bean("redisService")
    @ConditionalOnMissingBean
    public RedisService redisService(@Autowired RedisTemplate<Object, Object> redisTemplate) {
        return new RedisServiceImpl(redisTemplate);
    }

    @Bean("redisRateLimitService")
    @ConditionalOnMissingBean(RateLimitService.class)
    public RateLimitService redisRateLimitService(RedisService redisService) {
        return new RedisRateLimitServiceImpl(redisService);
    }
}
