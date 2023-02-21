package com.smart.auth.security;

import com.smart.auth.core.authentication.MethodPermissionEvaluatorImpl;
import com.smart.auth.core.properties.AuthProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;

/**
 * 方法级权限控制配置类
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

}
