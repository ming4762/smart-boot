package com.smart.module.api.crud;

import com.smart.module.api.crud.module.SmartDataContextUserModel;
import com.smart.module.api.crud.module.SmartDataPermissionModel;

import java.util.List;

/**
 * 数据权限API
 * @author shizhongming
 * 2025/3/17 19:45
 * @since 5.0.0
 */
public interface SmartCrudDataPermissionApi {

    /**
     * 获取用户信息
     * @return 用户信息
     */
    SmartDataContextUserModel getUserContext();

    /**
     * 获取用户部门列表
     * @return 用户部门列表
     */
    List<Long> getUserDeptList();

    /**
     * 获取用户部门及子部门列表
     * @return 用户部门及子部门列表
     */
    List<Long> getUserDeptChildren();

    /**
     * 获取当前用户所有数据权限
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getCurrentUserDataPermission();

    /**
     * 根据code获取数据权限
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList);
}
