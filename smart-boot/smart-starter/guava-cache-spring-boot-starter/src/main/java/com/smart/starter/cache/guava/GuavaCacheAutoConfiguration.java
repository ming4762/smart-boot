package com.smart.starter.cache.guava;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
public class GuavaCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GuavaCacheService guavaCacheService() {
        return new GuavaCacheServiceImpl();
    }
}
