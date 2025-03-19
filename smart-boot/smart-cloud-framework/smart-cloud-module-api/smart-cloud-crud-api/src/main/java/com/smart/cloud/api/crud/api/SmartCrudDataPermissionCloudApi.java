package com.smart.cloud.api.crud.api;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.module.api.crud.SmartCrudDataPermissionApi;
import com.smart.module.api.crud.module.SmartDataContextUserModel;
import com.smart.module.api.crud.module.SmartDataPermissionModel;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDeptDTO;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * crud模块数据权限API-spring cloud
 * @author shizhongming
 * 2025/3/17 20:43
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartCrudDataPermissionCloudApi implements SmartCrudDataPermissionApi {

    private final SysUserApi sysUserApi;

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public SmartDataContextUserModel getUserContext() {
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
     *
     * @return 用户部门列表
     */
    @Override
    public List<Long> getUserDeptList() {
        return this.sysUserApi.listUserDept(
                        SysUserDeptParameter.builder()
                                .userId(AuthUtils.getCurrentUserId())
                                .build()
                ).stream()
                .map(SysDeptDTO::getDeptId)
                .toList();
    }

    /**
     * 获取用户部门及子部门列表
     *
     * @return 用户部门及子部门列表
     */
    @Override
    public List<Long> getUserDeptChildren() {
        return this.sysUserApi.listUserDeptWithChildren(
                        SysUserDeptParameter.builder()
                                .userId(AuthUtils.getCurrentUserId())
                                .build()
                ).stream()
                .map(SysDeptDTO::getDeptId)
                .toList();
    }

    /**
     * 获取当前用户所有数据权限
     *
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getCurrentUserDataPermission() {
        return List.of();
    }

    /**
     * 根据code获取数据权限
     *
     * @param codeList 数据权限编码
     * @return 数据权限
     */
    @Override
    public List<SmartDataPermissionModel> getDataPermissionByCode(List<String> codeList) {
        // TODO: 待实现
        return List.of();
    }
}
