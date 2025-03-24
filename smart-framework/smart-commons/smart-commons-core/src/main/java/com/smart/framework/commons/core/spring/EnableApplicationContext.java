package com.smart.framework.commons.core.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用spring 上下文工具类
 * @author jackson
 * 2020/3/12 9:55 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ApplicationContextConfig.class)
public @interface EnableApplicationContext {
}
