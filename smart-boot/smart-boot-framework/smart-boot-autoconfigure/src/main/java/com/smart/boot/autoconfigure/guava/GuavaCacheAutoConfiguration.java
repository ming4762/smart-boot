package com.smart.boot.autoconfigure.guava;

import com.smart.framework.cache.guava.GuavaCacheService;
import com.smart.framework.cache.guava.GuavaCacheServiceImpl;
import com.smart.framework.cache.guava.limit.GuavaRateLimitServiceImpl;
import com.smart.framework.commons.core.lock.limit.RateLimitService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(GuavaCacheService.class)
public class GuavaCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GuavaCacheService guavaCacheService() {
        return new GuavaCacheServiceImpl();
    }

    @Bean("guavaRateLimitService")
    @ConditionalOnMissingBean(RateLimitService.class)
    public RateLimitService guavaRateLimitService() {
        return new GuavaRateLimitServiceImpl();
    }
}
