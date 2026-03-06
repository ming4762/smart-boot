package com.smart.framework.auth.extensions.jwt;

import com.google.common.collect.Lists;
import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.core.config.SmartAuthDomainConfig;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.constants.DefaultAuthUrlEnum;
import com.smart.framework.auth.core.filter.SmartAuthenticationFilter;
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
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
@Slf4j
public class AuthJwtSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H, AuthJwtSecurityConfigurer<H>> {

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
                .addFilterAfter(this.createJwtFilterChainProxy(builder), BasicAuthenticationFilter.class)
                .addFilterAfter(this.postProcess(new JwtRefreshTokenLoginFilter(this.getRefreshTokenUrl())), ExceptionTranslationFilter.class);
        if (Boolean.TRUE.equals(this.serviceProvider.jwtAuth)) {
            // 添加认证过滤器
            builder.addFilterAfter(this.postProcess(new SmartAuthenticationFilter(authProperties.getIgnores(), authProperties.getDevelopment())), JwtRefreshTokenLoginFilter.class);
        }
    }

    /**
     * 初始化函数
     * @param builder HttpSecurity
     */
    @Override
    public void init(H builder) throws Exception {
        super.init(builder);
        builder.setSharedObject(SecurityContextRepository.class, this.getBean(SecurityContextRepository.class));
        // 创建上下文
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
        this.addSharedCaptchaLoginUrl(builder, this.getLoginUrls());
    }

    /**
     * 创建jwt 拦截器链
     * @return 拦截器链
     */
    private FilterChainProxy createJwtFilterChainProxy(H builder) {
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 创建登录过滤器
        chains.addAll(this.createWebLoginFilter(builder));

        // 创建logout过滤器
        chains.add(new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(this.getLogoutUrl()), this.jwtLogoutFilter(builder)));

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
     * 获取登出路径
     * @return 登出路径
     */
    private String getLogoutUrl() {
        return DefaultAuthUrlEnum.LOGOUT.getUrl();
    }

    /**
      * 获取刷新令牌路径
     *
     * @return 刷新令牌路径
     */
    private String getRefreshTokenUrl() {
        return DefaultAuthUrlEnum.REFRESH.getUrl();
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
                AuthDomainConstants.AUTH_DOMAIN_ADMIN,
                SmartAuthDomainConfig.builder()
                        .loginUrl(DefaultAuthUrlEnum.LOGIN.getUrl())
                        .build()
        );
    }

    /**
     * 获取登录路径
     * @return 登录路径
     */
    private Set<String> getLoginUrls() {
        return this.getAuthDomainConfig().values().stream()
                .map(SmartAuthDomainConfig::getLoginUrl)
                .collect(Collectors.toSet());
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

    /**
     * 服务配置类
     */
    @Setter
    private static class ServiceProvider {
        /**
         * 是否使用jwt认证器
         */
        private Boolean jwtAuth;

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
