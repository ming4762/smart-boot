package com.smart.auth.cache.guava;

import com.smart.auth.cache.guava.cache.GuavaAuthCache;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shizhongming
 * 2024/9/10 17:23
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class SmartAuthGuavaAutoConfiguration {

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
