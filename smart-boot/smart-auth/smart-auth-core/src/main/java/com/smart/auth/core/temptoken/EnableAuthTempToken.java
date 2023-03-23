package com.smart.auth.core.temptoken;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启临时token支持
 * @author ShiZhongMing
 * 2021/3/10 9:51
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({AuthTempTokenBeanConfig.class, AuthTempTokenConfig.class})
public @interface EnableAuthTempToken {
}
