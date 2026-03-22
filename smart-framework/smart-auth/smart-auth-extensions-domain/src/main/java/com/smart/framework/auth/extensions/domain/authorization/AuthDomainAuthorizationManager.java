package com.smart.framework.auth.extensions.domain.authorization;

import com.smart.framework.auth.common.annotation.AuthDomain;
import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.authentication.SmartAuthDomainAuthentication;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.utils.AuthCheckUtils;
import com.smart.framework.commons.core.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

/**
 * 领域权限验证
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-25 09:58
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class AuthDomainAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    private final ObjectProvider<AuthProperties> authPropertiesProvider;

    @Override
    public @Nullable AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, MethodInvocation methodInvocation) {
        if (this.ignore()) {
            return new AuthorizationDecision(true);
        }
        Authentication authentication = authenticationSupplier.get();
        if (authentication instanceof SmartAuthDomainAuthentication authDomainAuthentication && authDomainAuthentication.isNonAuthDomain()) {
            // 没有启用认证域，默认授权
            return new AuthorizationDecision(true);
        }
        RestUserDetails restUserDetails = (RestUserDetails) authentication.getPrincipal();
        // 获取用户当前登录的认证域
        String currentAuthDomain = restUserDetails.getCurrentAuthDomain();
        // 查询接口所需权限域，如果未配置默认ADMIN
        List<String> requiredAuthDomains = Optional.ofNullable(this.findAuthDomain(methodInvocation))
                .map(AuthDomain::value)
                .map(Arrays::asList)
                .orElse(List.of(AuthDomainConstants.AUTH_DOMAIN_ADMIN));
        // 判断当前登录的认证域是否在接口所需的认证域列表中
        boolean granted = currentAuthDomain != null && requiredAuthDomains.contains(currentAuthDomain);
        if (!granted) {
            log.warn("用户 {} 无权限访问接口 {}，所需权限域 {}，当前登录权限域 {}",
                    restUserDetails.getUsername(), this.getMethodName(methodInvocation), requiredAuthDomains, currentAuthDomain);
        }
        return new AuthorizationDecision(granted);
    }

    /**
     * 查找方法上的AuthDomain注解
     * 如果方法上没有AuthDomain注解，则查找类上的AuthDomain注解
     * @param methodInvocation 方法调用
     * @return AuthDomain注解
     */
    private AuthDomain findAuthDomain(MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        AuthDomain annotation = AnnotationUtils.getAnnotation(method, AuthDomain.class);
        if (annotation != null) {
            return annotation;
        }
        return AnnotationUtils.getAnnotation(methodInvocation.getThis().getClass(), AuthDomain.class);
    }

    /**
     * 是否需要检查权限
     * @return 是否需要检查权限
     */
    protected boolean ignore() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return true;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        AuthProperties authProperties = this.authPropertiesProvider.getIfAvailable();
        if (authProperties == null) {
            throw new SystemException("获取AuthProperties失败");
        }
        return AuthCheckUtils.checkIgnores(request, authProperties.getIgnores());
    }

    protected String getMethodName(MethodInvocation methodInvocation) {
        return methodInvocation.getThis().getClass().getName() + "#" +methodInvocation.getMethod().getName();
    }
}
