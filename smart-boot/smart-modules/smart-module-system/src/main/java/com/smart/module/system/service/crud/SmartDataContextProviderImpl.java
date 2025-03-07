package com.smart.module.system.service.crud;

import com.smart.framework.auth.core.userdetails.RestUserDetails;
import com.smart.framework.auth.core.utils.AuthUtils;
import com.smart.framework.crud.datapermission.model.SmartDataContextUserModel;
import com.smart.framework.crud.datapermission.provider.SmartDataContextProvider;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysDeptDTO;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限上下文提供器
 * @author shizhongming
 * 2025/3/7 20:32
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class SmartDataContextProviderImpl implements SmartDataContextProvider {

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
}
