package com.smart.auth.autoconfigure.secret;

import com.smart.auth.cache.redis.AuthRedisCacheConfigure;
import com.smart.auth.cache.redis.secret.RedisAccessTokenStore;
import com.smart.auth.cache.redis.secret.RedisAppNoncestrStore;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.store.AccessTokenStore;
import com.smart.auth.core.secret.store.AppNoncestrStore;
import com.smart.auth.extensions.appsecret.AuthAppsecretSecurityConfigurer;
import com.smart.starter.redis.service.RedisService;
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
        AuthRedisCacheConfigure.class
})
public class AuthAppsecretRedisStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AccessTokenStore.class)
    public AccessTokenStore redisAccessTokenStore(AuthProperties properties) {
        return new RedisAccessTokenStore(properties);
    }

    /**
     * 创建redis随机串存储器
     * @param authProperties AuthProperties
     * @param redisService RedisService
     * @return AppNoncestrStore
     */
    @Bean
    @ConditionalOnMissingBean(AppNoncestrStore.class)
    public AppNoncestrStore redisAppNoncestrStore(AuthProperties authProperties, RedisService redisService) {
        return new RedisAppNoncestrStore(authProperties, redisService);
    }
}
