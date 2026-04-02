package com.smart.boot.guava;

import com.smart.boot.autoconfigure.cache.SmartCacheProperties;
import com.smart.framework.cache.guava.GuavaCacheService;
import com.smart.framework.cache.guava.GuavaCacheServiceImpl;
import com.smart.framework.cache.guava.limit.GuavaRateLimitServiceImpl;
import com.smart.framework.commons.core.lock.limit.RateLimitService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GuavaCacheService.class)
@EnableConfigurationProperties(SmartCacheProperties.class)
public class GuavaCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GuavaCacheService guavaCacheService(SmartCacheProperties smartCacheProperties) {
        return new GuavaCacheServiceImpl(smartCacheProperties.getPrefix());
    }

    @Bean("guavaRateLimitService")
    @ConditionalOnMissingBean(RateLimitService.class)
    public RateLimitService guavaRateLimitService() {
        return new GuavaRateLimitServiceImpl();
    }
}
