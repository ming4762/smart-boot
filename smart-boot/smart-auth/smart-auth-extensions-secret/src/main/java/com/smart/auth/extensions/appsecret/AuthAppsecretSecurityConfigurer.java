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
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class AuthAppsecretSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private AuthAppsecretSecurityConfigurer() {}

    public static AuthAppsecretSecurityConfigurer appsecret() {
        return new AuthAppsecretSecurityConfigurer();
    }

    @Override
    public void init(HttpSecurity builder) {
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
        try {
            return Optional.ofNullable(this.serviceProvider.applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }


    /**
     * 获取服务配置类
     * @return 服务配置类
     */
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    public class ServiceProvider {
        /**
         * 必须设置 spring上下文
         */
        private ApplicationContext applicationContext;

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

        public ServiceProvider applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public ServiceProvider signProvider(SignProvider signProvider) {
            this.signProvider = signProvider;
            return this;
        }

        public ServiceProvider AppNoncestrStore(AppNoncestrStore appNoncestrStore) {
            this.appNoncestrStore = appNoncestrStore;
            return this;
        }

        public ServiceProvider appsecretAuthenticationProvider(AppsecretAuthenticationProvider appsecretAuthenticationProvider) {
            this.appsecretAuthenticationProvider = appsecretAuthenticationProvider;
            return this;
        }

        public ServiceProvider appLoginSuccessHandler(AppLoginSuccessHandler appLoginSuccessHandler) {
            this.appLoginSuccessHandler = appLoginSuccessHandler;
            return this;
        }

        public ServiceProvider authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
            this.authenticationFailureHandler = authenticationFailureHandler;
            return this;
        }


        public ServiceProvider loginUrl(String url) {
            this.loginUrl = url;
            return this;
        }

        public ServiceProvider accessTokenStore(AccessTokenStore accessTokenStore) {
            this.accessTokenStore = accessTokenStore;
            return this;
        }

        public AuthAppsecretSecurityConfigurer and() {
            return AuthAppsecretSecurityConfigurer.this;
        }
    }
}
