package com.smart.commons.core.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启限流支持注解
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RateLimitRegistrar.class)
public @interface EnableRateLimit {

    /**
     * 服务名
     * @return 服务器名
     */
    String service() default "";
}
