package com.smart.framework.auth.extensions.dingtalk;

import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.properties.AuthDingtalkProperties;
import com.smart.framework.auth.extensions.dingtalk.authentication.DingtalkAuthenticationProvider;
import com.smart.framework.auth.extensions.dingtalk.filter.DingtalkLoginFilter;
import com.smart.framework.auth.extensions.dingtalk.userdetails.DingtalkUserDetailService;
import com.smart.framework.extension.dingtalk.DingtalkApi;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉登录配置器
 * @author shizhongming
 * 2025/10/31 14:11
 * @since 5.0.0
 */
public class SmartAuthDingtalkConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    public void init(H builder) {
        // do nothing
    }

    @Override
    public void configure(H builder) {
        builder
                .authenticationProvider(this.createDingtalkAuthenticationProvider())
                .addFilterBefore(this.createLoginFilter(), BasicAuthenticationFilter.class);
    }

    /**
     * 创建钉钉认证提供者
     * 优先从容器中获取，没有则创建一个
     * @return DingtalkAuthenticationProvider
     */
    private DingtalkAuthenticationProvider createDingtalkAuthenticationProvider() {
        DingtalkAuthenticationProvider provider = this.getBean(DingtalkAuthenticationProvider.class);
        if (provider != null) {
            return provider;
        }
        DingtalkUserDetailService userDetailService = this.getBean(DingtalkUserDetailService.class);
        DingtalkApi dingtalkApi = this.getBean(DingtalkApi.class);
        AuthDingtalkProperties dingtalkProperties = this.getAuthProperties().getDingtalk();
        return new DingtalkAuthenticationProvider(dingtalkApi, dingtalkProperties, userDetailService);
    }

    /**
     * 创建钉钉登录过滤器
     * @return FilterChainProxy
     */
    private FilterChainProxy createLoginFilter() {
        List<SecurityFilterChain> chains = new ArrayList<>(1);
        DingtalkLoginFilter loginFilter = new DingtalkLoginFilter(this.serviceProvider.loginUrl);
        loginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        loginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        loginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler));
        chains.add(
                new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(this.serviceProvider.loginUrl), loginFilter)
        );
        return new FilterChainProxy(chains);
    }

    /**
     * 设置钉钉登录路径
     * @param loginUrl 登录路径
     * @return SmartAuthDingtalkConfigurer
     */
    public SmartAuthDingtalkConfigurer<H> loginUrl(String loginUrl) {
        this.serviceProvider.loginUrl = loginUrl;
        return this;
    }

    /**
     * 设置钉钉登录成功处理器
     * @param authenticationSuccessHandler 登录成功处理器
     * @return SmartAuthDingtalkConfigurer
     */
    public SmartAuthDingtalkConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.serviceProvider.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }
    /**
     * 设置钉钉登录失败处理器
     * @param authenticationFailureHandler 登录失败处理器
     * @return SmartAuthDingtalkConfigurer
     */
    public SmartAuthDingtalkConfigurer<H> authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.serviceProvider.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

    private static class ServiceProvider {
        private static final String DEFAULT_LOGIN_URL = "/auth/dingtalk/webLogin";

        private String loginUrl;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        public ServiceProvider() {
            this.loginUrl = DEFAULT_LOGIN_URL;
        }
    }

}
