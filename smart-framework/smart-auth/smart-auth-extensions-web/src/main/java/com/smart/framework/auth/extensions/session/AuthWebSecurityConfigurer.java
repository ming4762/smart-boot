package com.smart.framework.auth.extensions.session;

import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.filter.SmartAuthenticationFilter;
import com.smart.framework.auth.core.properties.AuthProperties;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * session 认证配置
 * @author shizhongming
 * 2025/3/12 14:14
 * @since 5.0.0
 */
public class AuthWebSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

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
    public void init(H builder) {
        AuthenticationManagerBuilder authenticationManagerBuilder = builder.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.parentAuthenticationManager(null);

        AuthenticationSuccessHandler successHandler = this.getBean(AuthenticationSuccessHandler.class, this.serviceProvider.authenticationSuccessHandler);
        builder.setSharedObject(AuthenticationSuccessHandler.class, successHandler);

        AuthenticationFailureHandler authenticationFailureHandler = this.getBean(AuthenticationFailureHandler.class, null);
        builder.setSharedObject(AuthenticationFailureHandler.class, authenticationFailureHandler);
    }

    @Override
    public void configure(H builder) {
        AuthProperties authProperties = this.getAuthProperties();
        builder.authenticationProvider(this.getRestAuthenticationProvider())
                .addFilterAfter(this.createWebLoginFilter(builder, authProperties.getLoginUrl(), authProperties.getBindIp()), BasicAuthenticationFilter.class);
        if (this.serviceProvider.authentication) {
            // 添加登录验证拦截器
            builder.addFilterAfter(this.postProcess(new SmartAuthenticationFilter(authProperties.getIgnores(), authProperties.getDevelopment())), ExceptionTranslationFilter.class);
        }
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
