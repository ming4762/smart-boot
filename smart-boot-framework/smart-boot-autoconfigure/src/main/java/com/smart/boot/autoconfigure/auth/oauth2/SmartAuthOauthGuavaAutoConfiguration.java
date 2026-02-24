package com.smart.boot.autoconfigure.auth.oauth2;

import com.smart.boot.autoconfigure.auth.cache.SmartAuthGuavaAutoConfiguration;
import com.smart.framework.auth.cache.guava.cache.GuavaAuthCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

/**
 * 配置类，用于自动配置 Redis 缓存的 OAuth2
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-23 19:15
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({GuavaAuthCache.class, OAuth2Authorization.class})
@AutoConfigureAfter(SmartAuthGuavaAutoConfiguration.class)
public class SmartAuthOauthGuavaAutoConfiguration {


}
