package com.smart.auth.autoconfigure.jwt;

import com.smart.auth.cache.guava.AuthGuavaCacheConfigure;
import com.smart.auth.cache.guava.cache.GuavaAuthCache;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.AuthJwtConfigure;
import com.smart.starter.cache.guava.GuavaCacheService;
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
        AuthGuavaCacheConfigure.class
})
@Configuration(proxyBeanMethods = false)
public class AuthJwtGuavaCacheAutoConfiguration {

    /**
     * 创建guavaAuthCache
     * @param authProperties authProperties
     * @return guavaAuthCache
     */
    @Bean
    @ConditionalOnMissingBean(AuthCache.class)
    public AuthCache<String, Object> guavaAuthCache(AuthProperties authProperties, GuavaCacheService cacheService) {
        return new GuavaAuthCache(authProperties, cacheService);
    }
}
