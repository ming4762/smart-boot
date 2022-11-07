package com.smart.sample.system.config;

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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity http, ApplicationContext applicationContext) {
        super.configure(http);
        http
                .formLogin().disable()
                .logout().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // jwt配置
                .apply(AuthJwtSecurityConfigurer.jwt())
                .serviceProvider()
                .applicationContext(applicationContext)
                .and()
                .and()
                // 验证码配置
                .apply(AuthCaptchaSecurityConfigurer.captcha())
                .serviceProvider()
                .applicationContext(applicationContext)
                .loginUrl(AuthJwtSecurityConfigurer.LOGIN_URL);
        return http.build();
    }

}
