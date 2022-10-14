package com.smart.crud.datapermission;

import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
public interface UserDataScopeProvider {

    /**
     * 获取当前用户的数据权限范围
     * @return 数据权限范围
     */
    @NonNull
    Set<String> getCurrentUserDataScope();

    /**
     * 获取用户的部门ID
     * @return 部门ID
     */
    Long getUserDeptId();

    /**
     * 获取用户ID
     * @return 用户ID
     */
    Long getUserId();

    /**
     * 获取用户部门及下级部门ID
     * @return 用户部门及下级部门ID
     */
    Set<Long> getUserAllDeptId();

}
