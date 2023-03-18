package com.smart.cloud.common.auth.config;

import com.smart.auth.core.handler.AuthAccessDeniedHandler;
import com.smart.auth.core.handler.RestAuthenticationEntryPoint;
import com.smart.cloud.common.auth.repository.RemoteSecurityContextRepository;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * @author zhongming4762
 * 2023/3/9 21:07
 */
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @SneakyThrows(Exception.class)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RemoteSecurityContextRepository securityContextRepository) {
        http
                .csrf().disable()
                .cors()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new AuthAccessDeniedHandler())
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .setSharedObject(SecurityContextRepository.class, securityContextRepository);

        return http.build();
    }
}
