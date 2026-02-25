package com.smart.framework.auth.common.annotation;

import com.smart.framework.auth.common.constants.AuthDomainConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识权限域，只有相应权限域的用户才能访问标记的接口或类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-25 09:36
 * @since 5.0.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthDomain {

    /**
     * 权限域，默认值为空数组
     * @return 权限域数组
     */
    String[] value() default {AuthDomainConstants.AUTH_DOMAIN_ADMIN};
}
