package com.smart.auth.security;

import com.smart.auth.core.authentication.AuthenticationFailureEventInitializer;
import com.smart.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.auth.core.authentication.url.DefaultUrlAuthenticationProviderImpl;
import com.smart.auth.core.authentication.url.UrlAuthenticationProvider;
import com.smart.auth.core.beans.DefaultUrlMappingProvider;
import com.smart.auth.core.beans.UrlMappingProvider;
import com.smart.auth.core.handler.AuthLoginFailureHandler;
import com.smart.auth.core.handler.AuthLoginSuccessHandler;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.handler.DefaultAuthSuccessDataHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.security.config.AuthMethodSecurityConfig;
import com.smart.auth.security.userdetails.DefaultUserDetailsServiceImpl;
import com.smart.module.api.system.SystemAuthUserApi;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * AUTH 自动配置类
 * @author shizhongming
 * 2021/1/2 9:28 上午
 */
@Configuration("AuthSecurity2AutoConfiguration")
@EnableConfigurationProperties(AuthProperties.class)
@Import(AuthMethodSecurityConfig.class)
@ComponentScan(basePackages = {"com.smart.auth.security.controller", "com.smart.auth.security.api"})
public class AuthSecurity2AutoConfiguration {

    /**
     * 创建默认的 UserDetailsService
     * @param systemAuthUserApi systemAuthUserApi
     * @return UserDetailsService
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public DefaultUserDetailsServiceImpl defaultUserDetailsService(SystemAuthUserApi systemAuthUserApi) {
        return new DefaultUserDetailsServiceImpl(systemAuthUserApi);
    }

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
        return new MethodPermissionEvaluatorImpl(authProperties);
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

    @SneakyThrows(Exception.class)
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
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

}
