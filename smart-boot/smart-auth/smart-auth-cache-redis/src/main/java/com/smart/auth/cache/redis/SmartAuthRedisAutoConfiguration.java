package com.smart.auth.cache.redis;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.starter.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/10 17:23
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartAuthRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthCache.class)
    public AuthCache<String, Object> redisAuthCache(RedisService redisService, AuthProperties authProperties) {
        return new RedisAuthCache(redisService, authProperties.getPrefix());
    }
}
