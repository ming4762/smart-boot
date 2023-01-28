package com.smart.auth.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * 方法级权限配置
 * @author ShiZhongMing
 * 2021/1/5 12:26
 * @since 1.0
 */
@EnableMethodSecurity
@ConditionalOnProperty(prefix = "gc.auth", name = "method", havingValue = "true")
public class AuthMethodSecurityConfig {

    private final PermissionEvaluator permissionEvaluator;

    public AuthMethodSecurityConfig(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(this.permissionEvaluator);
        return expressionHandler;
    }
}
