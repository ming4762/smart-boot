package com.smart.auth.extensions.jwt;

import com.google.common.collect.Lists;
import com.smart.auth.core.authentication.RestAuthenticationProvider;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.context.JwtContext;
import com.smart.auth.extensions.jwt.context.JwtSecurityContextRepository;
import com.smart.auth.extensions.jwt.filter.JwtAuthenticationFilter;
import com.smart.auth.extensions.jwt.filter.JwtLoginFilter;
import com.smart.auth.extensions.jwt.filter.JwtLogoutFilter;
import com.smart.auth.extensions.jwt.service.JwtService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessEventPublishingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
@Slf4j
public class AuthJwtSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    /**
     * 默认的登录地址
     */
    public static final String LOGIN_URL = "/auth/login";

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private JwtService jwtService;

    private JwtContext jwtContext;

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

    /**
     * 初始化bean
     */
    private void initBean() {
        if (Objects.isNull(this.getAuthProperties())) {
            Assert.notNull(this.serviceProvider.authProperties, "properties is null, please init properties");
        }
        if (Objects.isNull(this.getAuthCache())) {
            Assert.notNull(this.serviceProvider.authCache, "auth cache is null, please init it");
        }
        this.jwtService = this.getBean(JwtService.class, this.jwtService);
        if (Objects.isNull(this.jwtService)) {
            this.jwtService = new JwtService();
        }

    }

    private AuthProperties getAuthProperties() {
        return this.getBean(AuthProperties.class, this.serviceProvider.authProperties);
    }

    private AuthCache<String, Object> getAuthCache() {
        return this.getBean(AuthCache.class, this.serviceProvider.authCache);
    }

    private String getLoginUrl() {
        return Optional.ofNullable(this.serviceProvider.loginUrl).orElse(LOGIN_URL);
    }

    @Override
    public void configure(H builder) {
        // 构建
        builder
                .authenticationProvider(this.getAuthenticationProvider())
                // 添加登录 登出过滤器
                .addFilterAfter(this.createJwtFilterChainProxy(), BasicAuthenticationFilter.class);
        if (Boolean.TRUE.equals(this.serviceProvider.jwtAuth)) {
            // 添加认证过滤器
            builder.addFilterAfter(this.postProcess(new JwtAuthenticationFilter(this.jwtContext)), ExceptionTranslationFilter.class);
        }
    }

    private RestAuthenticationProvider getAuthenticationProvider() {
        UserDetailsService userDetailsService = this.getBean(UserDetailsService.class, null);
        return new RestAuthenticationProvider(userDetailsService);
    }

    /**
     * 初始化函数
     * @param builder HttpSecurity
     */
    @Override
    public void init(H builder) {
        builder.setSharedObject(SecurityContextRepository.class, this.postProcess(new JwtSecurityContextRepository()));
        // 初始化bean
        this.initBean();
        // 创建上下文
        this.jwtContext = this.createJwtContext();
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    /**
     * 创建jwt 拦截器链
     * @return 拦截器链
     */
    private FilterChainProxy createJwtFilterChainProxy() {
        final List<SecurityFilterChain> chains = Lists.newArrayList();
        // 创建登录过滤器
        final JwtLoginFilter jwtLoginFilter = this.jwtLoginFilter();
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getLoginUrl()), jwtLoginFilter));

        // 创建logout过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getLogoutUrl()), this.jwtLogoutFilter()));

        return new FilterChainProxy(chains);
    }

    /**
     * 创建登录过滤器
     * @return 登录过滤器
     */
    private JwtLoginFilter jwtLoginFilter() {
        final JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(this.jwtContext, this.serviceProvider.bindIp);
        jwtLoginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        jwtLoginFilter.setFilterProcessesUrl(this.getLoginUrl());

        jwtLoginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler));
        // 设置登录失败handler
        jwtLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.serviceProvider.authenticationFailureHandler));
        return jwtLoginFilter;
    }

    /**
     * 创建登出过滤器
     * @return 登出过滤器
     */
    private JwtLogoutFilter jwtLogoutFilter() {
        // 创建LogoutHandler
        List<LogoutHandler> logoutHandlerList = this.serviceProvider.logoutHandlerList;
        if (CollectionUtils.isEmpty(logoutHandlerList)) {
            logoutHandlerList = Lists.newArrayList(this.getBean(SecurityLogoutHandler.class, null));
        }
        // 添加登出通知类
        logoutHandlerList.add(this.postProcess(new LogoutSuccessEventPublishingLogoutHandler()));
        final JwtLogoutFilter logoutFilter = new JwtLogoutFilter(this.getBean(LogoutSuccessHandler.class, this.serviceProvider.logoutSuccessHandler), logoutHandlerList.toArray(new LogoutHandler[]{}));
        logoutFilter.setFilterProcessesUrl(this.getLogoutUrl());
        return logoutFilter;
    }

    /**
     * 获取登出地址
     * @return 登出地址
     */
    protected String getLogoutUrl() {
        return Optional.ofNullable(this.serviceProvider.logoutUrl).orElse(JwtLogoutFilter.LOGOUT_URL);
    }

    /**
     * 创建JWT 上下文
     * @return JWT上下文
     */
    private JwtContext createJwtContext() {
        return JwtContext.builder()
                .loginUrl(this.getLoginUrl())
                .logoutUrl(this.serviceProvider.logoutUrl)
                .authProperties(this.getBean(AuthProperties.class, this.serviceProvider.authProperties))
                .build();
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

    /**
     * 设置登录URL
     * @param loginUrl 登录URL
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> loginUrl(String loginUrl) {
        this.serviceProvider.setLoginUrl(loginUrl);
        return this;
    }

    /**
     * 设置登出URL
     * @param logoutUrl 登出URL
     * @return this
     */
    public AuthJwtSecurityConfigurer<H> logoutUrl(String logoutUrl) {
        this.serviceProvider.setLogoutUrl(logoutUrl);
        return this;
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

    public AuthJwtSecurityConfigurer<H> bindIp(boolean bindIp) {
        this.serviceProvider.bindIp = bindIp;
        return this;
    }

    /**
     * 服务配置类
     */
    @Setter
    private static class ServiceProvider {
        private AuthProperties authProperties;

        private AuthCache<String, Object> authCache;

        private String loginUrl;

        private String logoutUrl;
        /**
         * 是否使用jwt认证器
         */
        private Boolean jwtAuth;

        private AuthenticationSuccessHandler authenticationSuccessHandler;

        private AuthenticationFailureHandler authenticationFailureHandler;

        private List<LogoutHandler> logoutHandlerList;

        private LogoutSuccessHandler logoutSuccessHandler;


        /**
         * 是否绑定IP
         */
        private Boolean bindIp;

        public ServiceProvider() {
            this.bindIp = true;
            this.jwtAuth = true;
            this.logoutHandlerList = new ArrayList<>();
        }
    }

}
