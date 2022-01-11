package com.smart.auth.security;

import com.smart.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.auth.core.beans.DefaultUrlMappingProvider;
import com.smart.auth.core.beans.UrlMappingProvider;
import com.smart.auth.core.handler.AuthLoginFailureHandler;
import com.smart.auth.core.handler.AuthLoginSuccessHandler;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.handler.DefaultAuthSuccessDataHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthUserService;
import com.smart.auth.security.config.AuthMethodSecurityConfig;
import com.smart.auth.security.userdetails.DefaultUserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.PermissionEvaluator;
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
@ComponentScan(basePackages = "com.smart.auth.security.controller")
public class AuthSecurity2AutoConfiguration {

    /**
     * 创建默认的 UserDetailsService
     * @param authUserService authUserService
     * @return UserDetailsService
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public DefaultUserDetailsServiceImpl defaultUserDetailsService(AuthUserService authUserService) {
        return new DefaultUserDetailsServiceImpl(authUserService);
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
    @ConditionalOnProperty(prefix = "gc.auth", name = "method", havingValue = "true")
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
    public DefaultUrlMappingProvider defaultUrlMappingProvider(RequestMappingHandlerMapping mapping) {
        return new DefaultUrlMappingProvider(mapping);
    }
}
