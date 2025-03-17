package com.smart.cloud.service.auth.config;

import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.extensions.session.AuthWebSecurityConfigurer;
import com.smart.module.auth.config.AuthCaptchaSecurityConfigurer;
import com.smart.module.auth.config.AuthWebSecurityConfigurerAdapter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, LogoutSuccessHandler logoutSuccessHandler) {
        super.configure(httpSecurity);

        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                .logout(config -> {
                    config.logoutUrl(this.authProperties.getLogoutUrl())
                            .logoutSuccessHandler(logoutSuccessHandler);
                })
                .sessionManagement(Customizer.withDefaults())
                // 启用web认证模式，但是关闭登录认证，交给gateway统一处理
                .with(AuthWebSecurityConfigurer.web(), configurer -> configurer.authentication(false))
                .with(AuthCaptchaSecurityConfigurer.captcha(), Customizer.withDefaults());
        return httpSecurity.build();
    }
}
