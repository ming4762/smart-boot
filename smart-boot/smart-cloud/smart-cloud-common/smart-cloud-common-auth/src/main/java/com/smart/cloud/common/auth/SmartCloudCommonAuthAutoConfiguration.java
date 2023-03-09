package com.smart.cloud.common.auth;

import com.smart.cloud.common.auth.config.SecurityConfig;
import com.smart.cloud.common.auth.repository.RemoteSecurityContextRepository;
import com.smart.module.api.auth.AuthApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/9
 */
@Configuration(proxyBeanMethods = false)
@Import(SecurityConfig.class)
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
                List.of(new ConcurrentMapCache(RemoteSecurityContextRepository.USER_CACHE_NAME))
        );
        return simpleCacheManager;
    }
}
