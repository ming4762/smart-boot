package com.smart.auth.autoconfigure.secret;

import com.smart.auth.cache.guava.AuthGuavaCacheConfigure;
import com.smart.auth.cache.guava.secret.GuavaAccessTokenStore;
import com.smart.auth.cache.guava.secret.GuavaAppNoncestrStore;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.store.AccessTokenStore;
import com.smart.auth.core.secret.store.AppNoncestrStore;
import com.smart.auth.extensions.appsecret.AuthAppsecretSecurityConfigurer;
import com.smart.starter.cache.guava.GuavaCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({
        AuthAppsecretSecurityConfigurer.class,
        AuthGuavaCacheConfigure.class
})
public class AuthAppsecretGuavaStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AccessTokenStore.class)
    public AccessTokenStore guavaAccessTokenStore(AuthProperties properties, GuavaCacheService cacheService) {
        return new GuavaAccessTokenStore(properties, cacheService);
    }

    @Bean
    @ConditionalOnMissingBean(AppNoncestrStore.class)
    public AppNoncestrStore guavaAppNoncestrStore(AuthProperties authProperties, GuavaCacheService cacheService) {
        return new GuavaAppNoncestrStore(authProperties, cacheService);
    }
}
