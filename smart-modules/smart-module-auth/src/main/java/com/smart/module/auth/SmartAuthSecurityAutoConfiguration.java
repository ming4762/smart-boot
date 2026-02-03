package com.smart.module.auth;

import com.smart.framework.auth.core.authentication.AuthenticationFailureEventInitializer;
import com.smart.framework.auth.core.authentication.DefaultSmartAuthenticationEventPublisher;
import com.smart.framework.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.framework.auth.core.authentication.SmartAuthenticationEventPublisher;
import com.smart.framework.auth.core.authentication.url.DefaultUrlAuthenticationProviderImpl;
import com.smart.framework.auth.core.authentication.url.UrlAuthenticationProvider;
import com.smart.framework.auth.core.beans.DefaultUrlMappingProvider;
import com.smart.framework.auth.core.beans.UrlMappingProvider;
import com.smart.framework.auth.core.event.AuthEventListener;
import com.smart.framework.auth.core.handler.*;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.token.CompositeSmartTokenRepository;
import com.smart.framework.auth.core.userdetails.DefaultUserDetailsBuilderImpl;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.auth.config.AuthMethodSecurityConfig;
import com.smart.module.auth.event.AuthEventLockedHandler;
import com.smart.module.auth.event.AuthEventLogHandler;
import com.smart.module.auth.remember.AuthCachePersistentTokenRepository;
import com.smart.module.auth.userdetails.RestUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * AUTH 自动配置类
 * @author shizhongming
 * 2021/1/2 9:28 上午
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthProperties.class)
@Import(AuthMethodSecurityConfig.class)
@ComponentScan(basePackages = {"com.smart.module.auth.controller", "com.smart.module.auth.api"})
public class SmartAuthSecurityAutoConfiguration {

    /**
     * 创建 AuthenticationSuccessHandler
     * @return AuthenticationSuccessHandler
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthLoginSuccessHandler();
    }

    /**
     * 创建登录失败执行器
     * @return AuthLoginFailureHandler
     */
    @Bean
    @ConditionalOnMissingBean(AuthLoginFailureHandler.class)
    public AuthLoginFailureHandler authLoginFailureHandler() {
        return new AuthLoginFailureHandler();
    }


    @Bean
    @ConditionalOnMissingBean(AuthSuccessDataHandler.class)
    public AuthSuccessDataHandler defaultAuthSuccessDataHandler() {
        return new DefaultAuthSuccessDataHandler();
    }

    /**
     * 自定义方法级权限认证器
     * @param authProperties 参数
     * @return 方法级权限认证器
     */
    @Bean
    @ConditionalOnBean(AuthorizationManagerBeforeMethodInterceptor.class)
    @ConditionalOnMissingBean(PermissionEvaluator.class)
    public PermissionEvaluator permissionEvaluator(AuthProperties authProperties) {
        return new MethodPermissionEvaluatorImpl(authProperties.getDevelopment());
    }

    /**
     * 创建默认的 UrlMappingProvider
     * @param mapping RequestMappingHandlerMapping
     * @return UrlMappingProvider
     */
    @Bean
    @ConditionalOnMissingBean(UrlMappingProvider.class)
    public DefaultUrlMappingProvider defaultUrlMappingProvider(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping mapping) {
        return new DefaultUrlMappingProvider(mapping);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationFailureEventInitializer authenticationFailureEventInitializer(DefaultAuthenticationEventPublisher eventPublisher) {
        return new AuthenticationFailureEventInitializer(eventPublisher);
    }

    /**
     * 创建URL权限认证器
     * @param urlMappingProvider URL映射
     * @return UrlAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public UrlAuthenticationProvider defaultUrlAuthenticationProviderImpl(UrlMappingProvider urlMappingProvider) {
        return new DefaultUrlAuthenticationProviderImpl(urlMappingProvider);
    }

    /**
     * 创建登录失败锁定器
     * @param sysUserApi SysUserApi
     * @return AuthEventLockedHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthEventLockedHandler authEventLockedHandler(SysUserApi sysUserApi) {
        return new AuthEventLockedHandler(sysUserApi);
    }

    /**
     * 创建登录日志处理器
     * @param sysLogApi 日志API
     * @return AuthEventLogHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthEventLogHandler authEventLogHandler(SysLogApi sysLogApi) {
        return new AuthEventLogHandler(sysLogApi);
    }

    /**
     * remember me token存储器
     * @param authCache authCache
     * @param authProperties authProperties
     * @return AuthCachePersistentTokenRepository
     */
    @Bean
    @ConditionalOnMissingBean
    public PersistentTokenRepository persistentTokenRepository(AuthCache<Object> authCache, AuthProperties authProperties) {
        return new AuthCachePersistentTokenRepository(authCache, authProperties);
    }

    /**
     * 创建 LogoutSuccessHandler
     * @return LogoutSuccessHandler
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AuthLogoutSuccessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(UserDetailsBuilder userDetailsBuilder, SystemAuthUserApi systemAuthUserApi) {
        return new RestUserDetailsServiceImpl(systemAuthUserApi, userDetailsBuilder);
    }

    /**
     * 创建认证事件监听器
     * @return 认证事件监听器
     */
    @Bean
    @ConditionalOnMissingBean(AuthEventListener.class)
    public AuthEventListener authEventListener() {
        return new AuthEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserDetailsBuilder userDetailsBuilder(SystemAuthUserApi systemAuthUserApi, CompositeSmartTokenRepository tokenRepository, AuthProperties authProperties) {
        return new DefaultUserDetailsBuilderImpl(systemAuthUserApi, tokenRepository, authProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartAuthenticationEventPublisher smartAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultSmartAuthenticationEventPublisher(applicationEventPublisher);
    }
}
