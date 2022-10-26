package com.smart.auth.security;

import com.smart.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.auth.core.properties.AuthProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * 方法级权限控制配置类
 * TODO:待完善：只有添加 {@link org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity}方法权限控制才会生效
 * TODO: 不添加注解的情况下该配置类无意义
 * @author ShiZhongMing
 * 2022/10/26
 * @since 3.0.0
 */
public class AuthMethodSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PermissionEvaluator methodPermissionEvaluatorImpl(AuthProperties authProperties) {
        return new MethodPermissionEvaluatorImpl(authProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            PermissionEvaluator permissionEvaluator,
            ObjectProvider<GrantedAuthorityDefaults> defaultsProvider,
            ApplicationContext context
    ) {
        var handler = new DefaultMethodSecurityExpressionHandler();
        defaultsProvider.ifAvailable((d) -> handler.setDefaultRolePrefix(d.getRolePrefix()));
        handler.setApplicationContext(context);
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }
}
