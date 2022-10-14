package com.smart.crud.datapermission;

/**
 * 数据权限类型
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
public enum DataPermissionScope {
    /**
     * 所有数据权限
     */
    DATA_ALL,

    /**
     * 部门数据权限
     */
    DATA_DEPT,

    /**
     * 部门及下级部门数据权限
     */
    DATA_DEPT_AND_CHILD,

    /**
     * 个人数据权限
     */
    DATA_PERSONAL;
}
