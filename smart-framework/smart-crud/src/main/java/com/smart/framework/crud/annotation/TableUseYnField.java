package com.smart.framework.crud.annotation;

import java.lang.annotation.*;

/**
 * 标记启用停用字段
 * @author shizhongming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableUseYnField {
}
