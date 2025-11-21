package com.smart.boot.autoconfigure.crud.annotation;


import com.smart.boot.autoconfigure.crud.config.SmartDataPermissionConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启数据权限
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/14 16:08
 * @since 5.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SmartDataPermissionConfiguration.class)
public @interface EnableDataPermission {
}
