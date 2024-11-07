package com.smart.framework.crud.annotation;

import org.apache.ibatis.mapping.SqlCommandType;

import java.lang.annotation.*;

/**
 * 标记租户ID字段
 * @author shizhongming
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface TableTenantField {

    /**
     * 排除的 SQL命令类型
     * @return 排除的 SQL命令类型
     */
    SqlCommandType[] ignoreCommands() default {};

    /**
     * 平台管理租户忽略的命令
     * @return SQL命令类型
     */
    SqlCommandType[] platformTenantIgnoreCommands() default {};
}
