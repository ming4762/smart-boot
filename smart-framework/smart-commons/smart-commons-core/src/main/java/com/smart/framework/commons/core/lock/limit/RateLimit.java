package com.smart.framework.commons.core.lock.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * 限流注解
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流的key
     * @return key
     */
    String value() default "";

    /**
     * 单位时间内访问次数限制
     * @return 限制
     */
    long limit();

    /**
     * 时间单位
     * @return 时间单位
     */
    ChronoUnit unit() default ChronoUnit.SECONDS;

    /**
     * 提示
     * @return 提示内容
     */
    String message() default "The maximum access times limit is exceeded. Please try again later";
}
