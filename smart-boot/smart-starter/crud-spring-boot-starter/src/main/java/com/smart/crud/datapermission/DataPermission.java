package com.smart.crud.datapermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {

    /**
     * 部门字段所在表别名
     * @return 别名
     */
    String deptAlias();

    /**
     * 用户字段表别名
     * @return 别名
     */
    String userAlias();

    /**
     * 是否使用自动注入SQL的方式
     * true：使用mybatis过滤器自动注入sql
     * @return 是否自动注入sql
     */
    boolean autoInjection() default false;
}
