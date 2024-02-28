package com.smart.cloud.common.auth.config;

import com.smart.auth.core.handler.AuthAccessDeniedHandler;
import com.smart.auth.core.handler.RestAuthenticationEntryPoint;
import com.smart.cloud.common.auth.repository.RemoteSecurityContextRepository;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
        http.csrf(AbstractHttpConfigurer::disable)
                        .cors(Customizer.withDefaults())
                .exceptionHandling(
                        configurer -> configurer.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                                .accessDeniedHandler(new AuthAccessDeniedHandler())
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .setSharedObject(SecurityContextRepository.class, securityContextRepository);
        return http.build();
    }
}
