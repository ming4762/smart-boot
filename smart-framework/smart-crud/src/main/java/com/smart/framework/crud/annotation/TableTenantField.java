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

    /**
     *  是否默认字段
     *  如果存在多个租户字段，必须存在默认租户字段
     * @return 是否默认租户字段
     */
    boolean isDefault() default true;
}
