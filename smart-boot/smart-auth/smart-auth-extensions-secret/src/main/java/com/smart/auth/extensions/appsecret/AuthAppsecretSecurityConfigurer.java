package com.smart.auth.extensions.appsecret;

import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.secret.store.AccessTokenStore;
import com.smart.auth.core.secret.store.AppNoncestrStore;
import com.smart.auth.extensions.appsecret.authentication.AppsecretAuthenticationProvider;
import com.smart.auth.extensions.appsecret.filter.AppsecretAuthenticationFilter;
import com.smart.auth.extensions.appsecret.filter.AppsecretLoginFilter;
import com.smart.auth.extensions.appsecret.handler.AppLoginSuccessHandler;
import com.smart.auth.extensions.appsecret.sign.SignProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Objects;
import java.util.Optional;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Slf4j
public class AuthAppsecretSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private AuthAppsecretSecurityConfigurer() {}

    public static <H extends HttpSecurityBuilder<H>> AuthAppsecretSecurityConfigurer<H> appsecret() {
        return new AuthAppsecretSecurityConfigurer<>();
    }

    public H config(Customizer<AuthAppsecretSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder
                .authenticationProvider(this.getBean(AppsecretAuthenticationProvider.class, this.serviceProvider.appsecretAuthenticationProvider))
                .addFilterBefore(this.createAppsecretLoginFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AppsecretAuthenticationFilter(
                        this.getBean(AuthProperties.class, null),
                        this.getBean(AppNoncestrStore.class, this.serviceProvider.appNoncestrStore),
                        this.getBean(AccessTokenStore.class,  this.serviceProvider.accessTokenStore),
                        this.getBean(SignProvider.class, this.serviceProvider.signProvider)
                        ), ExceptionTranslationFilter.class);
    }

    /**
     * 创建登录拦截器
     * @return 登录拦截器
     */
    protected AppsecretLoginFilter createAppsecretLoginFilter() {
        AppsecretLoginFilter appsecretLoginFilter = new AppsecretLoginFilter(this.serviceProvider.loginUrl);
        appsecretLoginFilter.setAuthenticationManager(this.getBean(AuthenticationManager.class, null));
        appsecretLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        appsecretLoginFilter.setAuthenticationSuccessHandler(this.getBean(AppLoginSuccessHandler.class, this.serviceProvider.appLoginSuccessHandler));
        return appsecretLoginFilter;
    }

    private <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }


    public AuthAppsecretSecurityConfigurer<H> loginUrl(String loginUrl) {
        this.serviceProvider.loginUrl = loginUrl;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.serviceProvider.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> accessTokenStore(AccessTokenStore accessTokenStore) {
        this.serviceProvider.accessTokenStore = accessTokenStore;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> appNoncestrStore(AppNoncestrStore appNoncestrStore) {
        this.serviceProvider.appNoncestrStore = appNoncestrStore;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> appLoginSuccessHandler(AppLoginSuccessHandler appLoginSuccessHandler) {
        this.serviceProvider.appLoginSuccessHandler = appLoginSuccessHandler;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> appsecretAuthenticationProvider(AppsecretAuthenticationProvider appsecretAuthenticationProvider) {
        this.serviceProvider.appsecretAuthenticationProvider = appsecretAuthenticationProvider;
        return this;
    }

    public AuthAppsecretSecurityConfigurer<H> signProvider(SignProvider signProvider) {
        this.serviceProvider.signProvider = signProvider;
        return this;
    }

    private static class ServiceProvider {
        /**
         * 登录地址
         */
        private String loginUrl;

        /**
         * 登录失败
         */
        private AuthenticationFailureHandler authenticationFailureHandler;


        private AccessTokenStore accessTokenStore;

        private AppNoncestrStore appNoncestrStore;

        private AppLoginSuccessHandler appLoginSuccessHandler;

        private AppsecretAuthenticationProvider appsecretAuthenticationProvider;

        private SignProvider signProvider;
    }
}
