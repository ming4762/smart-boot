package com.smart.module.system.constants;

/**
 * @author shizhongming
 * 2024/4/8 15:48
 * @since 3.0.0
 */
public enum SystemConstantEnum {
    TENANT_SUBSCRIBE_LIST_WITH_PACKAGE,

    /**
     * 是否根据租户过滤数据
     */
    LIST_FILTER_TENANT,

    /**
     * 查询租户信息
     */
    LIST_WITH_TENANT,

    LIST_USER_WITH_ACCOUNT,

    /**
     * 查询系统参数列表（支持分页、实体类属性查询），包含默认参数
     */
    LIST_PARAMETER_WITH_COMMON,
}
