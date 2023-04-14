package com.smart.cloud.auth.config;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.auth.security.config.AuthCaptchaSecurityConfigurer;
import com.smart.auth.security.config.AuthWebSecurityConfigurerAdapter;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 认证模块配置
 * @author zhongming4762
 * 2023/3/8
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig extends AuthWebSecurityConfigurerAdapter {

    public SecurityConfig(AuthProperties authProperties) {
        super(authProperties);
    }

    @SneakyThrows(Exception.class)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ApplicationContext applicationContext) {
        super.configure(http);
        http
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // jwt支持
                .and().apply(AuthJwtSecurityConfigurer.jwt())
                .serviceProvider()
                .jwtAuth(false)
                .bindIp(false)
                .and().and()
                // 验证码
                .apply(AuthCaptchaSecurityConfigurer.captcha())
                .serviceProvider()
                .applicationContext(applicationContext)
                .loginUrl(AuthJwtSecurityConfigurer.LOGIN_URL);
        return http.build();
    }
}
