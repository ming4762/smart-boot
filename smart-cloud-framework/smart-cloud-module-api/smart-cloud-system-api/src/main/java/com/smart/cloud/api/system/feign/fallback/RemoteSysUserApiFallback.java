package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysUserApi;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysDeptDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:52
 * @since 5.0.0
 */
@Component
public class RemoteSysUserApiFallback implements RemoteSysUserApi {
    @Override
    public List<SysUserDTO> listUserByUsername(List<String> usernameList) {
        return List.of();
    }

    @Override
    public List<SysUserDTO> listUserById(List<Long> userIdList) {
        return List.of();
    }

    @Override
    public boolean lockAccount(UserAccountLockDTO parameter) {
        return false;
    }

    @Override
    public boolean updateLoginFailTime(AccountLoginFailTimeUpdateDTO userId) {
        return false;
    }

    @Override
    public List<SysUserDTO> listUser(RemoteSysUserListParameter parameter) {
        return List.of();
    }

    @Override
    public List<SysDeptDTO> listUserDept(SysUserDeptParameter parameter) {
        return List.of();
    }

    @Override
    public List<SysDeptDTO> listUserDeptWithChildren(SysUserDeptParameter parameter) {
        return List.of();
    }
}
