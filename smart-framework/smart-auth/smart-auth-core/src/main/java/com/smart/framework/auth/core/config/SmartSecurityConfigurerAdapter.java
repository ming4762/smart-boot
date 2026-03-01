package com.smart.framework.auth.core.config;

import com.smart.framework.auth.core.authentication.RestAuthenticationProvider;
import com.smart.framework.auth.core.filter.WebLoginFilter;
import com.smart.framework.auth.core.matcher.ExtensionPathMatcher;
import com.smart.framework.auth.core.properties.AuthIgnoreProperties;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.utils.AuthCheckUtils;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author shizhongming
 * 2023/10/25 15:41
 * @since 3.0.0
 */
@Slf4j
public class SmartSecurityConfigurerAdapter<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

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
}
