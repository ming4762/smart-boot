package com.smart.module.auth.config;

import com.smart.framework.auth.core.handler.AuthAccessDeniedHandler;
import com.smart.framework.auth.core.handler.RestAuthenticationEntryPoint;
import com.smart.framework.auth.core.properties.AuthProperties;
import lombok.SneakyThrows;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * 默认的web配置器
 * @author ShiZhongMing
 * 2021/1/5 8:46
 * @since 1.0
 */
public class AuthWebSecurityConfigurerAdapter {

    protected final AuthProperties authProperties;

    public AuthWebSecurityConfigurerAdapter(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @SneakyThrows(Exception.class)
    protected void configure(HttpSecurity httpSecurity) {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                        .cors(Customizer.withDefaults())
                        .exceptionHandling(
                                configurer -> configurer.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                                                        .accessDeniedHandler(new AuthAccessDeniedHandler()));
//        this.ignore(httpSecurity);
//        // 开发模式不拦截
//        if (BooleanUtils.isTrue(this.authProperties.getDevelopment())) {
//            httpSecurity.authorizeHttpRequests(registry -> registry.anyRequest().permitAll());
//        }
    }
}
