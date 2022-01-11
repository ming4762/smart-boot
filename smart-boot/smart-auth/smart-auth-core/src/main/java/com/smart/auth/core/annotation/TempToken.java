package com.smart.auth.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识需要临时token
 * @author ShiZhongMing
 * 2021/3/8 17:52
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TempToken {

    /**
     * 是否要进行IP 校验
     * @return true or false
     */
    boolean ipValidate() default true;

    /**
     * 标识访问的资源
     * @return 资源
     */
    String resource();

}
