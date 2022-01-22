package com.smart.sample.system.config;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.auth.security.config.AuthCaptchaSecurityConfigurer;
import com.smart.auth.security.config.AuthWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

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

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
                .applicationContext(this.getApplicationContext())
                .and()
                .and()
                // 验证码配置
                .apply(AuthCaptchaSecurityConfigurer.captcha())
                .serviceProvider()
                .applicationContext(this.getApplicationContext())
                .loginUrl(AuthJwtSecurityConfigurer.LOGIN_URL);
    }
}
