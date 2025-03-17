package com.smart.boot.autoconfigure.auth.extension;

import com.smart.boot.autoconfigure.auth.session.RedisSessionRepositoryBeanPostProcessor;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.session.AuthWebSecurityConfigurer;
import com.smart.framework.auth.extensions.session.SmartSessionTokenRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.session.SessionIdGenerator;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * 基于SESSION认证自动配置1
 * @author shizhongming
 * 2025/3/12 16:20
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@EnableSpringHttpSession
@ConditionalOnClass(AuthWebSecurityConfigurer.class)
public class SmartAuthWebAutoConfiguration {

    /**
     * 创建基于用户信息的session id生成器
     * @return UserDataSessionIdGenerator
     */
    @Bean
    @ConditionalOnMissingBean(SessionIdGenerator.class)
    public SmartSessionTokenRepository smartSessionTokenRepository(AuthCache<Object> authCache) {
        return new SmartSessionTokenRepository(authCache);
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver(HttpHeaders.AUTHORIZATION);
    }

    @Bean
    public static RedisSessionRepositoryBeanPostProcessor redisSessionRepositoryBeanPostProcessor() {
        return new RedisSessionRepositoryBeanPostProcessor();
    }
}
