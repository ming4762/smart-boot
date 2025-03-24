package com.smart.framework.commons.core.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启多时区
 * @author shizhongming
 * 2025/1/25 19:12
 * @since 5.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MultiTimeZoneConfigurer.class)
public @interface EnableMultiTimeZone {
}
