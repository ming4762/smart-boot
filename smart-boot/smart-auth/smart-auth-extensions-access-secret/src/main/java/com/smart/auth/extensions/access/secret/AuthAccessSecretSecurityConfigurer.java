package com.smart.auth.extensions.access.secret;

import com.smart.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.AccessSecretProvider;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.access.secret.filter.AuthAccessSecretAuthenticationFilter;
import lombok.Setter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shizhongming
 * 2023/10/25 15:35
 * @since 3.0.0
 */
public class AuthAccessSecretSecurityConfigurer extends SmartSecurityConfigurerAdapter<HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    public static AuthAccessSecretSecurityConfigurer build() {
        return new AuthAccessSecretSecurityConfigurer();
    }

    public HttpSecurity config(Customizer<AuthAccessSecretSecurityConfigurer> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder.securityMatcher(this.getSecurityMatcher().toArray(new String[]{}))
                .addFilterAfter(
                        new AuthAccessSecretAuthenticationFilter(
                                this.getAuthProperties(),

                                this.getBean(AccessSecretProvider.class, null),
                                this.getBean(AuthCache.class, null)
                        ),
                        ExceptionTranslationFilter.class);
    }

    @Override
    public void init(HttpSecurity builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    private List<String> getSecurityMatcher() {
        if (!CollectionUtils.isEmpty(this.serviceProvider.urlMatcher)) {
            return this.serviceProvider.urlMatcher;
        }
        List<String> urlMatcher = this.getAuthProperties().getAccessSecret().getUrlMatcher();
        if (CollectionUtils.isEmpty(urlMatcher)) {
            throw new IllegalArgumentException("参数错误，accessSecret必须设置 url matcher");
        }
        return urlMatcher;
    }

    private AuthProperties getAuthProperties() {
        return Objects.requireNonNull(this.getBean(AuthProperties.class, null));
    }

    /**
     * 添加匹配的URL
     * 使用此函数添加的URL优先级高于配置文件中的
     * @param urlMatcher 匹配的路径
     * @return this
     */
    public AuthAccessSecretSecurityConfigurer addUrlMatcher(String ...urlMatcher) {
        this.serviceProvider.urlMatcher.addAll(List.of(urlMatcher));
        return this;
    }

    @Setter
    private static class ServiceProvider {

        private List<String> urlMatcher;

        ServiceProvider() {
            this.urlMatcher = new ArrayList<>(16);
        }
    }
}
