package com.smart.framework.commons.core.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 引入自定义的object com.smart.framework.tool.code.mapper
 * @author zhongming4762
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomObjectMapperConfigurer.class)
public @interface EnabledCustomObjectMapper {
}
