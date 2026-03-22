package com.smart.framework.auth.core.config;

import com.smart.framework.auth.core.authentication.RestAuthenticationProvider;
import com.smart.framework.auth.core.filter.WebLoginFilter;
import com.smart.framework.auth.core.matcher.ExtensionPathMatcher;
import com.smart.framework.auth.core.properties.AuthIgnoreProperties;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.share.AuthSharedCaptchaLoginUrl;
import com.smart.framework.auth.core.utils.AuthCheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author shizhongming
 * 2023/10/25 15:41
 * @since 3.0.0
 */
@Slf4j
public class SmartSecurityConfigurerAdapter<H extends HttpSecurityBuilder<H>, C extends SmartSecurityConfigurerAdapter<H, C>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private final Map<String, SmartAuthDomainConfig> authDomainConfigList = HashMap.newHashMap(16);

    /**
     * 添加权限域配置
     * @param authDomain 权限域
     * @param authDomainConfig 权限域配置
     * @return this
     */
    public C addAuthDomain(@NonNull String authDomain, @NonNull SmartAuthDomainConfig authDomainConfig) {
        return this.addAuthDomain(Map.of(authDomain, authDomainConfig));
    }

    /**
     * 添加权限域配置
     * @param authDomainConfigMap 权限域配置
     * @return this
     */
    public C addAuthDomain(@NonNull Map<String, SmartAuthDomainConfig> authDomainConfigMap) {
        authDomainConfigList.putAll(authDomainConfigMap);
        return (C) this;
    }

    /**
     * 获取权限域配置
     * @return 获取权限域
     */
    protected Map<String, SmartAuthDomainConfig> getAuthDomainConfig() {
        return Collections.unmodifiableMap(authDomainConfigList);
    }

    /**
     * 从容器中获取类
     * @param clazz 类型class
     * @param t 优先选中的值
     * @return T
     * @param <T> T
     */
    protected <T> T getBean(Class<T> clazz, T t) {
        if (Objects.nonNull(t)) {
            return t;
        }
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    protected <T> T getBean(Class<T> clazz) {
        return this.getBean(clazz, null);
    }

    protected <T> T getBeanOrElse(Class<T> clazz, Supplier<T> supplier) {
        return Optional.ofNullable(this.getBean(clazz)).orElseGet(supplier);
    }

    /**
     * 创建登录过滤器
     * @param builder build
     * @return 登录过滤器
     */
    protected List<DefaultSecurityFilterChain> createWebLoginFilter(H builder) {
        return this.getAuthDomainConfig().entrySet().stream()
                .map(item -> {
                    String authDomain = item.getKey();
                    SmartAuthDomainConfig authDomainConfig = item.getValue();
                    WebLoginFilter webLoginFilter = this.createWebLoginFilter(builder, authDomainConfig.getLoginUrl(), this.getAuthProperties().getBindIp());
                    webLoginFilter.setAuthDomain(authDomain);
                    return new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(authDomainConfig.getLoginUrl()), webLoginFilter);
                }).toList();
    }

    /**
     * 创建登录过滤器
     * @param builder 构建器
     * @param loginUrl 登录url
     * @param bindIp 是否绑定ip
     * @return WebLoginFilter
     */
    protected WebLoginFilter createWebLoginFilter(H builder, String loginUrl, boolean bindIp) {
        final WebLoginFilter webLoginFilter = new WebLoginFilter(loginUrl, bindIp);
        webLoginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        webLoginFilter.setFilterProcessesUrl(loginUrl);

        // 设置登录成功handler
        webLoginFilter.setAuthenticationSuccessHandler(builder.getSharedObject(AuthenticationSuccessHandler.class));
        // 设置登录失败handler
        webLoginFilter.setAuthenticationFailureHandler(builder.getSharedObject(AuthenticationFailureHandler.class));

        webLoginFilter.setSecurityContextRepository(builder.getSharedObject(SecurityContextRepository.class));

        RememberMeServices rememberMeServices = builder.getSharedObject(RememberMeServices.class);
        if (rememberMeServices != null) {
            webLoginFilter.setRememberMeServices(rememberMeServices);
        }
        return this.postProcess(webLoginFilter);
    }

    @Override
    public void init(H builder) {
        if (builder.getSharedObject(AuthenticationSuccessHandler.class) == null) {
            builder.setSharedObject(AuthenticationSuccessHandler.class, this.getBean(AuthenticationSuccessHandler.class));
        }
        if (builder.getSharedObject(AuthenticationFailureHandler.class) == null) {
            builder.setSharedObject(AuthenticationFailureHandler.class, this.getBean(AuthenticationFailureHandler.class));
        }
        super.init(builder);
    }

    /**
     * 获取认证配置
     * @return 认证配置
     */
    protected AuthProperties getAuthProperties() {
        return this.getBean(AuthProperties.class);
    }


    /**
     * 获取认证提供者
     * @return RestAuthenticationProvider
     */
    protected RestAuthenticationProvider getRestAuthenticationProvider() {
        RestAuthenticationProvider authenticationProvider = this.getBean(RestAuthenticationProvider.class);
        return Objects.requireNonNullElseGet(authenticationProvider, () -> new RestAuthenticationProvider(this.getBean(UserDetailsService.class)));
    }

    /**
     * 忽略请求
     * @return 忽略请求
     */
    protected Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> ignoreAuthorizeFromProperties() {
        return request -> {
            AuthIgnoreProperties ignoreProperties = this.getAuthProperties().getIgnores();
            List<ExtensionPathMatcher> extensionPathMatchers = AuthCheckUtils.createExtensionPathMatchers(ignoreProperties);
            if (CollectionUtils.isEmpty(extensionPathMatchers)) {
                // 没有需要忽略的请求
                request.anyRequest().authenticated();
                return;
            }
            request.requestMatchers(extensionPathMatchers.toArray(new ExtensionPathMatcher[0])).permitAll();

        };
    }

    /**
     * 添加需要校验登录验证码的URL
    * @param builder 构建器
    * @param loginUrl 登录url
     */
    protected void addSharedCaptchaLoginUrl(H builder, String loginUrl) {
        this.addSharedCaptchaLoginUrl(builder, Set.of(loginUrl));
    }

    /**
     * 添加需要校验登录验证码的URL
     * @param builder 构建器
     * @param loginUrls 登录url
     */
    protected void addSharedCaptchaLoginUrl(H builder, Set<String> loginUrls) {
        if (builder.getSharedObject(AuthSharedCaptchaLoginUrl.class) == null) {
            builder.setSharedObject(AuthSharedCaptchaLoginUrl.class, new AuthSharedCaptchaLoginUrl());
        }
        builder.getSharedObject(AuthSharedCaptchaLoginUrl.class).getLoginUrls().addAll(loginUrls);
    }

    /**
     * 获取需要校验登录验证码的URL
     * @param builder build
     * @return 需要校验登录验证码的URL
     */
    protected Set<String> getSharedCaptchaLoginUrls(H builder) {
        return builder.getSharedObject(AuthSharedCaptchaLoginUrl.class).getLoginUrls();
    }
}
