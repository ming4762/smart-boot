package com.smart.crud.datapermission;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * @author ShiZhongMing
 * 2022/10/14
 * @since 3.0.0
 */
public interface UserDataScopeProvider {

    /**
     * 获取用户数据权限
     * @return UserDataScope
     */
    @Nullable
    UserDataScope getUserUserDataScope();

    /**
     * 获取用户部门及下级部门ID
     * @param deptId 部门ID
     * @return 用户部门及下级部门ID
     */
    @NonNull
    Set<Long> getUserAllDeptId(@NonNull Long deptId);

    /**
     * 是否是超级管理员
     * @return 是否是超级管理员
     */
    boolean isSuperAdmin();
}
