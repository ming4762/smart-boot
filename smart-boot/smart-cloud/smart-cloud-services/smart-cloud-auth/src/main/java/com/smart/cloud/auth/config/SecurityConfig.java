package com.smart.cloud.auth.config;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.auth.security.config.AuthCaptchaSecurityConfigurer;
import com.smart.auth.security.config.AuthWebSecurityConfigurerAdapter;
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
                .with(AuthJwtSecurityConfigurer.jwt(), http -> http.jwtAuth(false).bindIp(false))
                .with(AuthCaptchaSecurityConfigurer.captcha(), Customizer.withDefaults());
        return httpSecurity.build();
    }
}
