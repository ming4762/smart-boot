package com.smart.auth.extensions.wechat;

import com.smart.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.auth.extensions.wechat.authentication.WechatAuthenticationProvider;
import com.smart.auth.extensions.wechat.filter.WechatAppLoginFilter;
import com.smart.auth.extensions.wechat.provider.WechatLoginProvider;
import com.smart.auth.extensions.wechat.userdetails.WechatUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
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
public class SmartAuthWechatAppConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private static final String DEFAULT_LOGIN_URL = "/wechat/auth/appLogin";

    public static <H extends HttpSecurityBuilder<H>> SmartAuthWechatAppConfigurer<H> wechatApp() {
        return new SmartAuthWechatAppConfigurer<>();
    }

    public H config(Customizer<SmartAuthWechatAppConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    private final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    public void init(H builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    @Override
    public void configure(H builder) {
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
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        List<WechatLoginProvider> wechatLoginProviderList = Arrays.stream(applicationContext.getBeanNamesForType(WechatLoginProvider.class))
                .map(item -> applicationContext.getBean(item, WechatLoginProvider.class))
                .toList();
        WechatUserDetailService wechatUserDetailService = this.getBean(WechatUserDetailService.class, null);
        WechatAuthConfigProvider wechatAuthConfigProvider = this.getBean(WechatAuthConfigProvider.class, null);
        return new WechatAuthenticationProvider(wechatLoginProviderList, wechatUserDetailService, wechatAuthConfigProvider);
    }

    private WechatAppLoginFilter createWechatAppLoginFilter() {
        WechatAppLoginFilter loginFilter = new WechatAppLoginFilter(this.serviceProvider.loginUrl);
        loginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        loginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        loginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler));
        return loginFilter;
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

    public SmartAuthWechatAppConfigurer<H> loginUrl(String loginUrl) {
        this.serviceProvider.loginUrl = loginUrl;
        return this;
    }

    public SmartAuthWechatAppConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.serviceProvider.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }

    public SmartAuthWechatAppConfigurer<H> authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.serviceProvider.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

    private static class ServiceProvider {
        private String loginUrl;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        public ServiceProvider() {
            this.loginUrl = DEFAULT_LOGIN_URL;
        }
    }

}
