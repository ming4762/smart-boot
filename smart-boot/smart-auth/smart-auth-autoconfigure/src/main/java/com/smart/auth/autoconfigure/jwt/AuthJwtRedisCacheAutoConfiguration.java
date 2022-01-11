package com.smart.auth.autoconfigure.jwt;

import com.smart.auth.cache.redis.AuthRedisCacheConfigure;
import com.smart.auth.cache.redis.RedisAuthCache;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.AuthJwtConfigure;
import com.smart.starter.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@ConditionalOnClass({
        AuthJwtConfigure.class,
        AuthRedisCacheConfigure.class
})
@Configuration(proxyBeanMethods = false)
public class AuthJwtRedisCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthCache.class)
    public RedisAuthCache redisAuthCache(RedisService redisService, AuthProperties authProperties) {
        return new RedisAuthCache(redisService, authProperties.getPrefix());
    }
}
