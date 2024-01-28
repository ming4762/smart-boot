package com.smart.auth.security.url;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
 * 动态URL校验
 * @author ShiZhongMing
 * 2021/1/4 16:20
 * @since 1.0
 */
public class UrlAuthenticationSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private UrlAuthenticationSecurityConfigurer() {}

    public static UrlAuthenticationSecurityConfigurer urlAuth() {
        return new UrlAuthenticationSecurityConfigurer();
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        UrlAuthorizationManager authorizationManager = new UrlAuthorizationManager();
        this.postProcess(authorizationManager);
        builder.authorizeHttpRequests(request -> request.anyRequest().access(authorizationManager));
    }
}
