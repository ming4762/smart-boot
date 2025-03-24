package com.smart.framework.crud.datapermission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解容器
 * @author shizhongming
 * @since 5.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SmartDataPermissions {

    SmartDataPermission[] value();
}
