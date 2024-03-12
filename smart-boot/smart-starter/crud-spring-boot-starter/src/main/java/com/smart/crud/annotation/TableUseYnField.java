package com.smart.crud.annotation;

import java.lang.annotation.*;

/**
 * 标记启用停用字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableUseYnField {
}
