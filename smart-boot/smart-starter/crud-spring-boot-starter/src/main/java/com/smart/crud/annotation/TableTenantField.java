package com.smart.crud.annotation;

import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.*;

/**
 * 标记租户ID字段
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableTenantField {

    /**
     * 排除的 SQL命令类型
     * @return 排除的 SQL命令类型
     */
    SqlCommandType[] excludeCommands() default {};
}
