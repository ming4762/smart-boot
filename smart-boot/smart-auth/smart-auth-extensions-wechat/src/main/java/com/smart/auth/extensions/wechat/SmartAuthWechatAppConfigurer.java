package com.smart.auth.extensions.wechat;

import com.smart.auth.core.userdetails.WechatUserDetailService;
import com.smart.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.auth.extensions.wechat.authentication.WechatAuthenticationProvider;
import com.smart.auth.extensions.wechat.filter.WechatAppLoginFilter;
import com.smart.auth.extensions.wechat.provider.WechatLoginProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.*;

/**
 * @author zhongming4762
 * 2023/4/3
 */
@Slf4j
public class SmartAuthWechatAppConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private HttpSecurity builder;

    private static final String DEFAULT_LOGIN_URL = "/wechat/auth/appLogin";

    public static SmartAuthWechatAppConfigurer wechatApp() {
        return new SmartAuthWechatAppConfigurer();
    }

    private final ServiceProvider serviceProvider = new ServiceProvider();
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    @Override
    public void init(HttpSecurity builder) {
        this.builder = builder;
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    @Override
    public void configure(HttpSecurity builder) {
        builder
                .authenticationProvider(this.createAuthenticationProvider())
                .addFilterBefore(this.createLoginFilter(), BasicAuthenticationFilter.class);
    }

    private FilterChainProxy createLoginFilter() {
        List<SecurityFilterChain> chains = new ArrayList<>(1);
        chains.add(
                new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.serviceProvider.loginUrl), this.createWechatAppLoginFilter())
        );
        return new FilterChainProxy(chains);
    }

    private WechatAuthenticationProvider createAuthenticationProvider() {
        ApplicationContext applicationContext = this.builder.getSharedObject(ApplicationContext.class);
        List<WechatLoginProvider> wechatLoginProviderList = Arrays.stream(applicationContext.getBeanNamesForType(WechatLoginProvider.class))
                .map(item -> applicationContext.getBean(item, WechatLoginProvider.class))
                .toList();
        WechatUserDetailService wechatUserDetailService = this.getBean(WechatUserDetailService.class, null);
        WechatAuthConfigProvider wechatAuthConfigProvider = this.getBean(WechatAuthConfigProvider.class, null);
        return new WechatAuthenticationProvider(wechatLoginProviderList, wechatUserDetailService, wechatAuthConfigProvider);
    }

    private WechatAppLoginFilter createWechatAppLoginFilter() {
        WechatAppLoginFilter loginFilter = new WechatAppLoginFilter(this.serviceProvider.loginUrl);
        loginFilter.setAuthenticationManager(this.builder.getSharedObject(AuthenticationManager.class));
        loginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        loginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler));
        return loginFilter;
    }

    private <T> T getBean(Class<T> clazz, T t) {
        ApplicationContext applicationContext = this.builder.getSharedObject(ApplicationContext.class);
        if (Objects.nonNull(t)) {
            return t;
        }
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

    public class ServiceProvider {
        private String loginUrl = DEFAULT_LOGIN_URL;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        public ServiceProvider loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public ServiceProvider authenticationSuccessHandler(AuthenticationFailureHandler authenticationFailureHandler) {
            this.authenticationFailureHandler = authenticationFailureHandler;
            return this;
        }


        public ServiceProvider authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
            this.authenticationSuccessHandler = authenticationSuccessHandler;
            return this;
        }

        public SmartAuthWechatAppConfigurer and() {
            return SmartAuthWechatAppConfigurer.this;
        }
    }

}
