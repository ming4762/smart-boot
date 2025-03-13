package com.smart.boot.autoconfigure.auth.cache;

import com.smart.framework.auth.cache.redis.RedisAuthCache;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.redis.service.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/10 17:23
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisAuthCache.class)
public class SmartAuthRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthCache.class)
    public AuthCache<String, Object> redisAuthCache(RedisService redisService, AuthProperties authProperties) {
        return new RedisAuthCache(redisService, authProperties.getPrefix());
    }

//    @Bean
//    @ConditionalOnClass(AuthWebSecurityConfigurer.class)
//    public SmartRedisSessionRepository smartRedisSessionRepository(RedisTemplate<String, Object> redisTemplate) {
//        return new SmartRedisSessionRepository(redisTemplate);
//    }
}
