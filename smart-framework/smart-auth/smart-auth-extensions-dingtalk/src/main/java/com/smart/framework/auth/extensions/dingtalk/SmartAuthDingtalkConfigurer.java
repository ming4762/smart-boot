package com.smart.framework.auth.extensions.dingtalk;

import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.core.config.SmartAuthDomainConfig;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.constants.DefaultAuthUrlEnum;
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
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 钉钉登录配置器
 * @author shizhongming
 * 2025/10/31 14:11
 * @since 5.0.0
 */
public class SmartAuthDingtalkConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H, SmartAuthDingtalkConfigurer<H>> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    @Override
    public void init(H builder) {
        // do nothing
    }

    @Override
    public void configure(H builder) {
        builder
                .authenticationProvider(this.createDingtalkAuthenticationProvider())
                .addFilterBefore(this.createLoginFilter(builder), BasicAuthenticationFilter.class);
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
    private FilterChainProxy createLoginFilter(H builder) {
        List<SecurityFilterChain> chains = new ArrayList<>(1);
        chains.addAll(this.createSecurityFilterChain(builder));
        return new FilterChainProxy(chains);
    }

    /**
     * 创建单个安全过滤器链
     * @param builder 构建器
     * @return SecurityFilterChain
     */
    private List<DefaultSecurityFilterChain> createSecurityFilterChain(H builder) {
        return this.getAuthDomainConfig().entrySet().stream().map(item -> {
            String authDomain = item.getKey();
            SmartAuthDomainConfig authDomainConfig = item.getValue();
            DingtalkLoginFilter loginFilter = new DingtalkLoginFilter(authDomainConfig.getLoginUrl());
            loginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
            loginFilter.setAuthenticationFailureHandler(Objects.requireNonNullElseGet(this.serviceProvider.authenticationFailureHandler, () -> builder.getSharedObject(AuthenticationFailureHandler.class)));
            loginFilter.setAuthenticationSuccessHandler(Objects.requireNonNullElseGet(this.serviceProvider.authenticationSuccessHandler, () -> builder.getSharedObject(AuthenticationSuccessHandler.class)));
            loginFilter.setAuthDomain(authDomain);
            return new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(authDomainConfig.getLoginUrl()), loginFilter);
        }).toList();
    }


    /**
     * 获取权限域配置
     *
     * @return 获取权限域
     */
    @Override
    protected Map<String, SmartAuthDomainConfig> getAuthDomainConfig() {
        Map<String, SmartAuthDomainConfig> authDomainConfig = super.getAuthDomainConfig();
        if (!CollectionUtils.isEmpty(authDomainConfig)) {
            return authDomainConfig;
        }
        return Map.of(
                AuthDomainConstants.AUTH_DOMAIN_NONE,
                SmartAuthDomainConfig.builder()
                        .loginUrl(DefaultAuthUrlEnum.DINGTALK_WEB_LOGIN.getUrl())
                        .build()
        );
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

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

    }

}
