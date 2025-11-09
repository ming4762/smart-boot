package com.smart.framework.auth.extensions.wechat;

import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.authentication.WechatAuthenticationProvider;
import com.smart.framework.auth.extensions.wechat.filter.WechatAppLoginFilter;
import com.smart.framework.auth.extensions.wechat.provider.WechatLoginProvider;
import com.smart.framework.auth.extensions.wechat.userdetails.WechatUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/3
 */
@Slf4j
public class SmartAuthWechatAppConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private static final String DEFAULT_LOGIN_URL = "auth/auth/appLogin";

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
                new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(this.serviceProvider.loginUrl), this.createWechatAppLoginFilter())
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
