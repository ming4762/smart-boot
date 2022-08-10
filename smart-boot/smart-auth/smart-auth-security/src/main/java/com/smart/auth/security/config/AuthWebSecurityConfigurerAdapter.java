package com.smart.auth.security.config;

import com.smart.auth.core.handler.AuthAccessDeniedHandler;
import com.smart.auth.core.handler.RestAuthenticationEntryPoint;
import com.smart.auth.core.matcher.ExtensionPathMatcher;
import com.smart.auth.core.properties.AuthProperties;
import lombok.SneakyThrows;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 默认的web配置器
 * @author ShiZhongMing
 * 2021/1/5 8:46
 * @since 1.0
 */
public class AuthWebSecurityConfigurerAdapter {

    private final AuthProperties authProperties;

    public AuthWebSecurityConfigurerAdapter(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @SneakyThrows(Exception.class)
    protected void configure(HttpSecurity http) {
        http.csrf().disable()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new AuthAccessDeniedHandler());
        this.ignore(http);
        // 开发模式不拦截
        if (BooleanUtils.isTrue(this.authProperties.getDevelopment())) {
            http.authorizeRequests().anyRequest().permitAll();
        }
    }

    @SneakyThrows(Exception.class)
    public void ignore(HttpSecurity http) {
        var ignoreConfig = this.authProperties.getIgnores();
        var registry = http.authorizeRequests();
        // 忽略 GET
        ignoreConfig.getGet().forEach(url -> this.doIgnore(registry, url, HttpMethod.GET));
        // 忽略 post
        ignoreConfig.getPost().forEach(url -> this.doIgnore(registry, url, HttpMethod.POST));
        // 忽略 DELETE
        ignoreConfig.getDelete().forEach(url -> this.doIgnore(registry, url, HttpMethod.DELETE));
        // 忽略 PUT
        ignoreConfig.getPut().forEach(url -> this.doIgnore(registry, url, HttpMethod.PUT));
        // 忽略 HEAD
        ignoreConfig.getHead().forEach(url -> this.doIgnore(registry, url, HttpMethod.HEAD));
        // 忽略 PATCH
        ignoreConfig.getPatch().forEach(url -> this.doIgnore(registry, url, HttpMethod.PATCH));
        // 忽略 OPTIONS
        ignoreConfig.getOptions().forEach(url -> this.doIgnore(registry, url, HttpMethod.OPTIONS));
        // 忽略 TRACE
        ignoreConfig.getTrace().forEach(url -> this.doIgnore(registry, url, HttpMethod.TRACE));
        // 按照请求格式忽略
        ignoreConfig.getPattern().forEach(url -> this.doIgnore(registry, url, null));
    }

    private void doIgnore(@NonNull ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry, @NonNull String url, @Nullable HttpMethod httpMethod) {
        var matcher = httpMethod == null ? new ExtensionPathMatcher(url) : new ExtensionPathMatcher(httpMethod, url);
        registry.requestMatchers(matcher).permitAll();
    }
}
