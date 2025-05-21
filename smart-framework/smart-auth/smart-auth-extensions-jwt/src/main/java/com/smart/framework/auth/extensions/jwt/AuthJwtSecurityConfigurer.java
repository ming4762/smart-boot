package com.smart.framework.auth.extensions.jwt;

import com.google.common.collect.Lists;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.filter.SmartAuthenticationFilter;
import com.smart.framework.auth.core.filter.WebLoginFilter;
import com.smart.framework.auth.core.handler.SecurityLogoutHandler;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.extensions.jwt.filter.JwtRefreshTokenLoginFilter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessEventPublishingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
@Slf4j
public class AuthJwtSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private AuthJwtSecurityConfigurer() {}

    /**
     * jwt 初始化
     * @return jwt
     */
    public static <H extends HttpSecurityBuilder<H>> AuthJwtSecurityConfigurer<H> jwt() {
        return new AuthJwtSecurityConfigurer<>();
    }

    public H config(Customizer<AuthJwtSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        AuthProperties authProperties = this.getAuthProperties();
        // 构建
        builder
                .authenticationProvider(this.getRestAuthenticationProvider())
                // 添加登录 登出过滤器
                .addFilterAfter(this.createJwtFilterChainProxy(builder), BasicAuthenticationFilter.class);
        if (Boolean.TRUE.equals(this.serviceProvider.jwtAuth)) {
            // 添加认证过滤器
            builder.addFilterAfter(this.postProcess(new SmartAuthenticationFilter(authProperties.getIgnores(), authProperties.getDevelopment())), ExceptionTranslationFilter.class)
                    .addFilterBefore(this.postProcess(new JwtRefreshTokenLoginFilter(authProperties.getRefreshTokenUrl())), SmartAuthenticationFilter.class);
        }
    }

    /**
     * 初始化函数
     * @param builder HttpSecurity
     */
    @Override
    public void init(H builder) {
        builder.setSharedObject(SecurityContextRepository.class, this.getBean(SecurityContextRepository.class));
        // 创建上下文
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);

        AuthenticationSuccessHandler successHandler = this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler);
        builder.setSharedObject(AuthenticationSuccessHandler.class, successHandler);

        AuthenticationFailureHandler authenticationFailureHandler = this.getBean(AuthenticationFailureHandler.class, null);
        builder.setSharedObject(AuthenticationFailureHandler.class, authenticationFailureHandler);
    }

    /**
     * 创建jwt 拦截器链
     * @return 拦截器链
     */
    private FilterChainProxy createJwtFilterChainProxy(H builder) {
        AuthProperties authProperties = this.getAuthProperties();
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 创建登录过滤器
        final WebLoginFilter webLoginFilter = this.createWebLoginFilter(builder, this.getLoginUrl(), authProperties.getBindIp());
        webLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getLoginUrl()), webLoginFilter));

        // 创建logout过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getLogoutUrl()), this.jwtLogoutFilter(builder)));

        return new FilterChainProxy(chains);
    }

    /**
     * 创建登出过滤器
     * @return 登出过滤器
     */
    private LogoutFilter jwtLogoutFilter(H builder) {
        // 创建LogoutHandler
        List<LogoutHandler> logoutHandlerList = this.serviceProvider.logoutHandlerList;
        if (CollectionUtils.isEmpty(logoutHandlerList)) {
            logoutHandlerList = Lists.newArrayList(this.getBean(SecurityLogoutHandler.class, null));
        }
        // 添加登出通知类
        logoutHandlerList.add(this.postProcess(new LogoutSuccessEventPublishingLogoutHandler()));
        // 添加remember me
        RememberMeServices rememberMeServices = builder.getSharedObject(RememberMeServices.class);
        if (rememberMeServices instanceof LogoutHandler logoutHandler) {
            logoutHandlerList.add(logoutHandler);
        }
        LogoutFilter logoutFilter = new LogoutFilter(this.getBean(LogoutSuccessHandler.class, this.serviceProvider.logoutSuccessHandler), logoutHandlerList.toArray(new LogoutHandler[]{}));
        logoutFilter.setFilterProcessesUrl(this.getLogoutUrl());
        return logoutFilter;
    }

    /**
     * 获取登出地址
     * @return 登出地址
     */
    protected String getLogoutUrl() {
        return this.getAuthProperties().getLogoutUrl();
    }


    private String getLoginUrl() {
        return this.getAuthProperties().getLoginUrl();
    }

    /**
     * 设置登录成功处理器
     * @param authenticationSuccessHandler 登录成功处理器
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.serviceProvider.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return this;
    }

    /**
     * 设置登录失败处理
     * @param authenticationFailureHandler 登录失败处理器
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.serviceProvider.setAuthenticationFailureHandler(authenticationFailureHandler);
        return this;
    }

    /**
     * 设置登出成功处理器
     * @param logoutSuccessHandler 登出成功处理器
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> logoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.serviceProvider.setLogoutSuccessHandler(logoutSuccessHandler);
        return this;
    }

    /**
     * 添加登出执行器
     * @param logoutHandler 登出执行器
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> addLogoutHandler(LogoutHandler logoutHandler) {
        this.serviceProvider.logoutHandlerList.add(logoutHandler);
        return this;
    }

    public AuthJwtSecurityConfigurer<H> jwtAuth(boolean jwtAuth) {
        this.serviceProvider.jwtAuth = jwtAuth;
        return this;
    }

    public AuthJwtSecurityConfigurer<H> rememberMe(boolean rememberMe) {
        this.serviceProvider.rememberMe = rememberMe;
        return this;
    }

    public AuthJwtSecurityConfigurer<H> development(boolean development) {
        this.serviceProvider.development = development;
        return this;
    }

    /**
     * 服务配置类
     */
    @Setter
    private static class ServiceProvider {
        /**
         * 是否使用jwt认证器
         */
        private Boolean jwtAuth;

        private Boolean development;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        private List<LogoutHandler> logoutHandlerList;

        private LogoutSuccessHandler logoutSuccessHandler;

        private Boolean rememberMe;


        public ServiceProvider() {
            this.jwtAuth = true;
            this.logoutHandlerList = new ArrayList<>();
        }
    }

}
