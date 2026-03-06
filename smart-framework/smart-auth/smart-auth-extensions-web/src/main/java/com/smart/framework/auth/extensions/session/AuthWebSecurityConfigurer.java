package com.smart.framework.auth.extensions.session;

import com.google.common.collect.Lists;
import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.core.config.SmartAuthDomainConfig;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.constants.DefaultAuthUrlEnum;
import com.smart.framework.auth.core.filter.SmartAuthenticationFilter;
import com.smart.framework.auth.core.properties.AuthProperties;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * session 认证配置
 * @author shizhongming
 * 2025/3/12 14:14
 * @since 5.0.0
 */
public class AuthWebSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H, AuthWebSecurityConfigurer<H>> {

    private AuthWebSecurityConfigurer() {}

    private final ServiceProvider serviceProvider = new ServiceProvider();

    public static <H extends HttpSecurityBuilder<H>> AuthWebSecurityConfigurer<H> web() {
        return new AuthWebSecurityConfigurer<>();
    }

    public H config(Customizer<AuthWebSecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void init(H builder) throws Exception {
        super.init(builder);
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);
    }

    @Override
    public void configure(H builder) {
        AuthProperties authProperties = this.getAuthProperties();
        builder.authenticationProvider(this.getRestAuthenticationProvider())
                .addFilterAfter(this.createWebFilterChainProxy(builder), BasicAuthenticationFilter.class);
        if (this.serviceProvider.authentication) {
            // 添加登录验证拦截器
            builder.addFilterAfter(this.postProcess(new SmartAuthenticationFilter(authProperties.getIgnores(), authProperties.getDevelopment())), ExceptionTranslationFilter.class);
        }
    }

    private FilterChainProxy createWebFilterChainProxy(H build) {
        List<SecurityFilterChain> chains = Lists.newArrayList();
        // 添加登录拦截器
        chains.addAll(this.createWebLoginFilter(build));
        return new FilterChainProxy(chains);
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
     * 设置登录成功处理器
     * @param authenticationSuccessHandler 登录成功处理器
     * @return this
     */
    public AuthWebSecurityConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.serviceProvider.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }

    public AuthWebSecurityConfigurer<H> authentication(boolean authentication) {
        this.serviceProvider.authentication = authentication;
        return this;
    }

    private static class ServiceProvider {
        private AuthenticationSuccessHandler authenticationSuccessHandler;
        private boolean authentication = true;
    }
}
