package com.smart.auth.autoconfigure.jwt;

import com.smart.auth.core.authentication.RestAuthenticationProvider;
import com.smart.auth.core.handler.AuthLogoutSuccessHandler;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.AuthJwtConfigure;
import com.smart.auth.extensions.jwt.handler.JwtAuthSuccessDataHandler;
import com.smart.auth.extensions.jwt.service.JwtService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthJwtConfigure.class)
public class AuthJwtAutoConfiguration {

    /**
     * 创建JwtService
     * @param authProperties 参数
     * @param authCache 缓存工具
     * @return JwtService
     */
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService(AuthProperties authProperties, AuthCache<String, Object> authCache) {
        return new JwtService(authProperties, authCache);
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

    /**
     * 创建 RestAuthenticationProvider
     * @param userDetailsService userDetailsService
     * @return RestAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean(RestAuthenticationProvider.class)
    public RestAuthenticationProvider restAuthenticationProvider(UserDetailsService userDetailsService) {
        return new RestAuthenticationProvider(userDetailsService);
    }

    /**
     * 创建 AuthSuccessDataHandler
     * @return JwtAuthSuccessDataHandler
     */
    @Bean
    @ConditionalOnMissingBean(JwtAuthSuccessDataHandler.class)
    public AuthSuccessDataHandler jwtAuthSuccessDataHandler() {
        return new JwtAuthSuccessDataHandler();
    }

}
