package com.smart.auth.extensions.jwt;

import com.google.common.collect.Lists;
import com.smart.auth.core.authentication.RestAuthenticationProvider;
import com.smart.auth.core.handler.AuthHandlerBuilder;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.context.JwtContext;
import com.smart.auth.extensions.jwt.context.JwtSecurityContextRepository;
import com.smart.auth.extensions.jwt.filter.JwtAuthenticationFilter;
import com.smart.auth.extensions.jwt.filter.JwtLoginFilter;
import com.smart.auth.extensions.jwt.filter.JwtLogoutFilter;
import com.smart.auth.extensions.jwt.service.JwtService;
import com.smart.auth.extensions.jwt.store.CacheJwtStore;
import com.smart.auth.extensions.jwt.store.JwtStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * JWT配置类
 * @author ShiZhongMing
 * 2020/12/31 14:58
 * @since 1.0
 */
@Slf4j
public class AuthJwtSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 默认的登录地址
     */
    public static final String LOGIN_URL = "/auth/login";

    private final ServiceProvider serviceProvider = new ServiceProvider();

    private final AuthHandlerBuilder handlerBuilder = new AuthHandlerBuilder();

    private JwtService jwtService;

    private JwtContext jwtContext;

    private AuthJwtSecurityConfigurer() {}

    /**
     * jwt 初始化
     * @return jwt
     */
    public static AuthJwtSecurityConfigurer jwt() {
        return new AuthJwtSecurityConfigurer();
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
            this.jwtService = new JwtService(this.getAuthProperties(), this.getBean(CacheJwtStore.class, null));
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

    /**
     * 初始化函数
     * @param builder HttpSecurity
     * @throws Exception Exception
     */
    @Override
    public void init(HttpSecurity builder) throws Exception {
        builder.setSharedObject(SecurityContextRepository.class, this.postProcess(new JwtSecurityContextRepository()));
        // 初始化bean
        this.initBean();
        // 创建上下文
        this.jwtContext = this.createJwtContext();
        // 构建
        builder
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(this.getBean(RestAuthenticationProvider.class, this.serviceProvider.authenticationProvider))
                // 添加登录 登出过滤器
                .addFilterAfter(this.createJwtFilterChainProxy(), BasicAuthenticationFilter.class)
                // 添加认证过滤器
                .addFilterAfter(new JwtAuthenticationFilter(this.jwtContext, this.getJwtStore()), ExceptionTranslationFilter.class);
    }

    protected List<JwtStore> getJwtStore() {
        if (this.serviceProvider.applicationContext == null) {
            return new ArrayList<>(0);
        }
        return Arrays.stream(this.serviceProvider.applicationContext.getBeanNamesForType(JwtStore.class))
                .map(item -> this.serviceProvider.applicationContext.getBean(item, JwtStore.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .collect(Collectors.toList());
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
        jwtLoginFilter.setFilterProcessesUrl(this.getLoginUrl());
        jwtLoginFilter.setAuthenticationManager(this.getBean(AuthenticationManager.class, null));
        // 设置登录成功handler
        jwtLoginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class, this.handlerBuilder.getAuthenticationSuccessHandler()));
        // 设置登录失败handler
        jwtLoginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class, this.handlerBuilder.getAuthenticationFailureHandler()));
        return jwtLoginFilter;
    }

    /**
     * 创建登出过滤器
     * @return 登出过滤器
     */
    private JwtLogoutFilter jwtLogoutFilter() {
        // 创建LogoutHandler
        List<LogoutHandler> logoutHandlerList = this.handlerBuilder.getLogoutHandlers();
        if (CollectionUtils.isEmpty(logoutHandlerList)) {
            logoutHandlerList = Lists.newArrayList(this.getBean(SecurityLogoutHandler.class, this.jwtService));
        }
        // 添加登出通知类
        logoutHandlerList.add(this.postProcess(new LogoutSuccessEventPublishingLogoutHandler()));
        final JwtLogoutFilter logoutFilter = new JwtLogoutFilter(this.getBean(LogoutSuccessHandler.class, this.handlerBuilder.getLogoutSuccessHandler()), logoutHandlerList.toArray(new LogoutHandler[]{}));
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
     * 获取服务配置类
     * @return 服务配置类
     */
    public ServiceProvider serviceProvider() {
        return this.serviceProvider;
    }

    /**
     * 创建handler Builder
     * @return handler Builder
     */
    public AuthHandlerBuilder authHandlerBuilder() {
        return this.handlerBuilder;
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
        try {
            return Optional.ofNullable(this.serviceProvider.applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 服务配置类
     */
    public class ServiceProvider {
        private AuthProperties authProperties;

        private AuthCache<String, Object> authCache;

        private ApplicationContext applicationContext;

        private String loginUrl;

        private String logoutUrl;

        /**
         * 是否绑定IP
         */
        private Boolean bindIp = Boolean.TRUE;

        private RestAuthenticationProvider authenticationProvider;

        public AuthJwtSecurityConfigurer and() {
            return AuthJwtSecurityConfigurer.this;
        }

        public ServiceProvider applicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            return this;
        }

        public ServiceProvider bindIp(boolean bindIp) {
            this.bindIp = bindIp;
            return this;
        }

        public ServiceProvider authenticationProvider(RestAuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
            return this;
        }

        public ServiceProvider properties(AuthProperties authProperties) {
            this.authProperties = authProperties;
            return this;
        }

        public ServiceProvider authCache(AuthCache<String, Object> authCache) {
            this.authCache = authCache;
            return this;
        }

        public ServiceProvider loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public ServiceProvider logoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
            return this;
        }

    }


}
