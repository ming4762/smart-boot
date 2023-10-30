package com.smart.service.system.config;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.extensions.access.secret.AuthAccessSecretSecurityConfigurer;
import com.smart.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.auth.extensions.sms.AuthSmsSecurityConfigurer;
import com.smart.auth.security.config.AuthCaptchaSecurityConfigurer;
import com.smart.auth.security.config.AuthWebSecurityConfigurerAdapter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author shizhongming
 * 2021/4/25 10:05 下午
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig extends AuthWebSecurityConfigurerAdapter {

    public SecurityConfig(AuthProperties authProperties) {
        super(authProperties);
    }

    @SneakyThrows(Exception.class)
    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity httpSecurity) {
        super.configure(httpSecurity);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                    .logout(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    // JWT配置
                    .apply(AuthJwtSecurityConfigurer.jwt())
                            .config(Customizer.withDefaults())
                    // 验证码配置
                    .apply(AuthCaptchaSecurityConfigurer.captcha())
                    .config(configurer -> configurer.addLoginUrl(AuthJwtSecurityConfigurer.LOGIN_URL))
                .apply(AuthSmsSecurityConfigurer.sms());
        return httpSecurity.build();
    }

    @SneakyThrows({Exception.class})
    @Bean
    @Order(Integer.MIN_VALUE)
    public SecurityFilterChain secretSecurityFilterChain(HttpSecurity httpSecurity) {
        super.configure(httpSecurity);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .apply(AuthAccessSecretSecurityConfigurer.build())
                .config(http -> http.addUrlMatcher("/access/api/**"));
        return httpSecurity.build();
    }

}
