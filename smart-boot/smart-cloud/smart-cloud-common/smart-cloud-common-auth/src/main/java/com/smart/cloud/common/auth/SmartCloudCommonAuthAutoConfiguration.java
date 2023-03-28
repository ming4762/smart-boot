package com.smart.cloud.common.auth;

import com.google.common.collect.ImmutableList;
import com.smart.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.temptoken.EnableAuthTempToken;
import com.smart.cloud.common.auth.config.SecurityConfig;
import com.smart.cloud.common.auth.repository.RemoteSecurityContextRepository;
import com.smart.module.api.auth.AuthApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;

/**
 * @author zhongming4762
 * 2023/3/9
 */
@Configuration(proxyBeanMethods = false)
@Import(SecurityConfig.class)
@EnableConfigurationProperties(AuthProperties.class)
@EnableAuthTempToken
public class SmartCloudCommonAuthAutoConfiguration {

    /**
     * 创建远程调用 SecurityContextRepository
     * @param cacheManager CacheManager
     * @param authApi AuthApi
     * @return RemoteSecurityContextRepository
     */
    @Bean
    public RemoteSecurityContextRepository remoteSecurityContextRepository(CacheManager cacheManager, AuthApi authApi) {
        return new RemoteSecurityContextRepository(cacheManager, authApi);
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(
                ImmutableList.of(new ConcurrentMapCache(RemoteSecurityContextRepository.USER_CACHE_NAME))
        );
        return simpleCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "smart.auth", name = "method", havingValue = "true", matchIfMissing = true)
    public PermissionEvaluator methodPermissionEvaluatorImpl(AuthProperties authProperties) {
        return new MethodPermissionEvaluatorImpl(authProperties.getDevelopment());
    }

    @Bean
    @ConditionalOnProperty(prefix = "smart.auth", name = "method", havingValue = "true", matchIfMissing = true)
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
    }
}
