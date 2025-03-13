package com.smart.cloud.service.auth.config;

import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.module.auth.config.AuthCaptchaSecurityConfigurer;
import com.smart.module.auth.config.AuthWebSecurityConfigurerAdapter;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ApplicationContext applicationContext) {
        super.configure(httpSecurity);

        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                    .sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .with(AuthJwtSecurityConfigurer.jwt(), http -> http.jwtAuth(false))
                .with(AuthCaptchaSecurityConfigurer.captcha(), Customizer.withDefaults());
        return httpSecurity.build();
    }
}
