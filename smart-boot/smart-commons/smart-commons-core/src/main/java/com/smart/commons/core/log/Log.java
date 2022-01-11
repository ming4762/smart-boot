package com.smart.commons.core.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 * @author jackson
 * 2020/1/22 1:55 下午
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 操作
     * @return 操作说明
     */
    String value();

    /**
     * 类型
     * @return 日志类型
     */
    LogOperationTypeEnum type() default LogOperationTypeEnum.QUERY;

    /**
     * 是否保存返回值
     * @return 是否保存返回值
     */
    boolean saveResult() default false;
}
