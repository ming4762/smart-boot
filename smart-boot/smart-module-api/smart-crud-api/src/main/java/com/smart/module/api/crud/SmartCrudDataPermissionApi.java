package com.smart.module.api.crud;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
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
    default SmartDataContextUserModel getUserContext() {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return SmartDataContextUserModel.builder()
                .userId(currentUser.getUserId())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .tenantId(currentUser.getUserTenant().getTenantId())
                .tenantCode(currentUser.getUserTenant().getTenantCode())
                .isSuperAdmin(AuthUtils.isSuperAdmin())
                .build();
    }

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
     * 根据mapperId获取数据权限
     * @param mapperId mapperId
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getDataPermissionByMapper(String mapperId);

    /**
     * 根据code获取数据权限
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList);
}
