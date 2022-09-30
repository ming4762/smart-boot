package com.smart.autoconfigure.compatible;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ShiZhongMing
 * 2022/9/30
 * @since 3.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableAutoConfigurationImportSelector.class)
public @interface CompatibleAutoConfiguration {

    Class<?>[] exclude() default {};

    String[] excludeName() default {};
}
