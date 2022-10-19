package com.smart.system.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.crud.datapermission.DataPermissionScope;
import com.smart.crud.datapermission.UserDataScope;
import com.smart.crud.datapermission.UserDataScopeProvider;
import com.smart.system.constants.UserDeptIdentEnum;
import com.smart.system.model.SysUserDeptPO;
import com.smart.system.service.SysDeptService;
import com.smart.system.service.SysUserDeptService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/10/17
 * @since 3.0.0
 */
@Component
public class UserDataScopeProviderImpl implements UserDataScopeProvider {

    private final SysUserDeptService sysUserDeptService;

    private final SysDeptService sysDeptService;

    public UserDataScopeProviderImpl(SysUserDeptService sysUserDeptService, SysDeptService sysDeptService) {
        this.sysUserDeptService = sysUserDeptService;
        this.sysDeptService = sysDeptService;
    }

    @Override
    public UserDataScope getUserUserDataScope() {
        var userId = AuthUtils.getNonNullCurrentUserId();
        var dataList = this.sysUserDeptService.list(
                new QueryWrapper<SysUserDeptPO>().lambda()
                        .select(SysUserDeptPO::getUserId, SysUserDeptPO::getDeptId, SysUserDeptPO::getIdent)
                        .eq(SysUserDeptPO::getUserId, userId)
                        .eq(SysUserDeptPO::getIdent, UserDeptIdentEnum.USER_DEPT)
        );
        if (dataList.isEmpty()) {
            return null;
        }
        var userDept = dataList.get(0);
        return new UserDataScope(userId, userDept.getDeptId(),
                Arrays.stream(userDept.getDataScope().split(","))
                        .map(DataPermissionScope::valueOf)
                        .collect(Collectors.toSet())
                );
    }

    @NonNull
    @Override
    public Set<Long> getUserAllDeptId(@NonNull Long deptId) {
        var deptIds = this.sysDeptService.queryAllChildIds(Set.of(deptId));
        deptIds.add(deptId);
        return deptIds;
    }


    @Override
    public boolean isSuperAdmin() {
        return AuthUtils.isSuperAdmin();
    }
}
