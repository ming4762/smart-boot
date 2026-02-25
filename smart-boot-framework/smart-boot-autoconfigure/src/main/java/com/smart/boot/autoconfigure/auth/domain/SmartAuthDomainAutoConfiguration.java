package com.smart.boot.autoconfigure.auth.domain;

import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.extensions.domain.authorization.AuthDomainAuthorizationManager;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.stereotype.Controller;

/**
 * 权限域自动配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-25 16:29
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthDomainAuthorizationManager.class)
public class SmartAuthDomainAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthDomainAuthorizationManager.class)
    public AuthDomainAuthorizationManager authDomainAuthorizationManager(AuthProperties authProperties) {
        return new AuthDomainAuthorizationManager(authProperties);
    }

    /**
     * 权限域授权自定义拦截器
     * @param authorizationManager 权限域授权管理器
     * @return 权限域授权自定义拦截器
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor authDomainAuthorizationManagerAdvisor(AuthDomainAuthorizationManager authorizationManager) {
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Controller.class, true);
        AuthorizationManagerBeforeMethodInterceptor interceptor =
                new AuthorizationManagerBeforeMethodInterceptor(pointcut, authorizationManager);
        // 在其他拦截器之前执行
        interceptor.setOrder(AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() - 1);
        return interceptor;
    }
}
