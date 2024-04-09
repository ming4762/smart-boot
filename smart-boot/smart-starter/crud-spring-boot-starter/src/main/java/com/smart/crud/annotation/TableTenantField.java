package com.smart.crud.annotation;

import java.lang.annotation.*;

/**
 * 标记租户ID字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableTenantField {
}
