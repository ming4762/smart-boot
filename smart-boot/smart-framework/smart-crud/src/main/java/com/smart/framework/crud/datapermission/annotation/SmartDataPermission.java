package com.smart.framework.crud.datapermission.annotation;

import com.smart.module.api.crud.constants.DataPermissionScopeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 * @author shizhongming
 * 2023/3/21 16:57
 * @since 5.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SmartDataPermission {

    /**
     * 数据权限过滤字段
     * @return 默认根据数据权限范围自动设置
     */
    String column() default "";

    /**
     * 数据权限范围
     * @return 数据范围，默认所有数据权限
     */
    DataPermissionScopeEnum scope() default DataPermissionScopeEnum.DATA_ALL;

    /**
     * 配置编码
     * @return 在页面配置的数据编码
     */
    String configCode() default "";

    /**
     * 需要加数据权限的表名
     * @return 表名
     */
    String tableName() default "";

}
