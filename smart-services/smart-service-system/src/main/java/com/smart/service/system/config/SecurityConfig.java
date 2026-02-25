package com.smart.service.system.config;

import com.smart.auth.extensions.access.secret.AuthAccessSecretSecurityConfigurer;
import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.remember.SmartAuthPersistentTokenRememberMeServices;
import com.smart.framework.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.module.auth.config.AuthCaptchaSecurityConfigurer;
import com.smart.module.auth.config.AuthTenantSecurityConfigurer;
import com.smart.module.auth.config.AuthWebSecurityConfigurerAdapter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.UUID;

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

    @Bean
    public SmartAuthPersistentTokenRememberMeServices smartAuthPersistentTokenRememberMeServices(UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        String key = UUID.randomUUID().toString();
        return new SmartAuthPersistentTokenRememberMeServices(key, userDetailsService, tokenRepository);
    }

    @SneakyThrows(Exception.class)
    @Bean
    @Order(0)
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity httpSecurity, SmartAuthPersistentTokenRememberMeServices rememberMeServices, AuthenticationSuccessHandler authenticationSuccessHandler, LogoutSuccessHandler logoutSuccessHandler) {
        super.configure(httpSecurity);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(
                        config -> config.rememberMeServices(rememberMeServices)
                                .authenticationSuccessHandler(authenticationSuccessHandler)
                )
//                .logout(config -> {
//                    config.logoutUrl(this.authProperties.getLogoutUrl())
//                            .logoutSuccessHandler(logoutSuccessHandler);
//                })
//                .sessionManagement(Customizer.withDefaults())
//                .with(AuthWebSecurityConfigurer.web(), Customizer.withDefaults())
                // JWT配置
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .with(AuthJwtSecurityConfigurer.jwt(), jwt -> jwt.authDomain(AuthDomainConstants.AUTH_DOMAIN_ADMIN))
                // 验证码配置
                .with(AuthCaptchaSecurityConfigurer.captcha(), Customizer.withDefaults())
                // 短信登录支持
//                .with(AuthSmsSecurityConfigurer.sms(), Customizer.withDefaults())
                // 租户支持
                .with(AuthTenantSecurityConfigurer.tenant(), Customizer.withDefaults());
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
                .securityMatcher("/access/api/**")
                .with(AuthAccessSecretSecurityConfigurer.build(), Customizer.withDefaults());
        return httpSecurity.build();
    }
}
