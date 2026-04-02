package com.smart.boot.auth.cache.redis.oauth2;

import com.smart.boot.auth.cache.redis.SmartAuthRedisAutoConfiguration;
import com.smart.framework.auth.cache.redis.RedisAuthCache;
import com.smart.framework.auth.cache.redis.oauth2.RedisOAuth2AuthorizationConsentService;
import com.smart.framework.auth.cache.redis.oauth2.RedisOAuth2AuthorizationService;
import com.smart.framework.auth.core.properties.AuthProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * 配置类，用于自动配置 Redis 缓存的 OAuth2
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-23 19:15
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedisAuthCache.class, OAuth2Authorization.class})
@AutoConfigureAfter(SmartAuthRedisAutoConfiguration.class)
public class SmartAuthOauthRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OAuth2AuthorizationConsentService.class)
    public RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService(RedisAuthCache redisAuthCache) {
        return new RedisOAuth2AuthorizationConsentService(redisAuthCache);
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2AuthorizationService.class)
    public RedisOAuth2AuthorizationService redisOAuth2AuthorizationService(AuthProperties authProperties, RedisAuthCache redisAuthCache) {
        return new RedisOAuth2AuthorizationService(authProperties, redisAuthCache);
    }
}
