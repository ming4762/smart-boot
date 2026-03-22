package com.smart.module.system.api.remote;

import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.*;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import com.smart.module.api.system.parameter.SysUserThirdAccountParameter;
import com.smart.module.system.api.local.LocalSysUserApi;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@RestController
@RequestMapping
public class RemoteSysUserApiController implements SysUserApi {

    private final LocalSysUserApi localSysUserApi;

    public RemoteSysUserApiController(LocalSysUserApi localSysUserApi) {
        this.localSysUserApi = localSysUserApi;
    }

    /**
     * 通过用户名查询用户
     *
     * @param usernameList 用户名列表
     * @return List
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER_BY_USERNAME)
    public List<SysUserDTO> listUserByUsername(@RequestBody List<String> usernameList) {
        return this.localSysUserApi.listUserByUsername(usernameList);
    }

    /**
     * 通过ID查询用户
     *
     * @param userIdList 用户ID列表
     * @return List
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER_BY_ID)
    public List<SysUserDTO> listUserById(@RequestBody List<Long> userIdList) {
        return this.localSysUserApi.listUserById(userIdList);
    }

    /**
     * 锁定账户
     *
     * @param parameter 参数
     * @return 账户锁定参数
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LOCK_ACCOUNT)
    public boolean lockAccount(@RequestBody UserAccountLockDTO parameter) {
        return this.localSysUserApi.lockAccount(parameter);
    }

    /**
     * 重置登录失败次数
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.RESET_LOGIN_FAIL_TIME)
    public boolean updateLoginFailTime(@RequestBody AccountLoginFailTimeUpdateDTO userId) {
        return this.localSysUserApi.updateLoginFailTime(userId);
    }

    /**
     * 查询用户列表
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER)
    public List<SysUserDTO> listUser(@RequestBody RemoteSysUserListParameter parameter) {
        return this.localSysUserApi.listUser(parameter);
    }

    /**
     * 查询用户部门列表
     *
     * @param parameter 参数
     * @return 用户部门列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.QUERY_USER_DEPT)
    public List<SysDeptDTO> listUserDept(SysUserDeptParameter parameter) {
        return this.localSysUserApi.listUserDept(parameter);
    }

    /**
     * 查询用户部门及子部门列表
     *
     * @param parameter 参数
     * @return 用户部门及子部门列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.QUERY_USER_DEPT_WITH_CHILDREN)
    public List<SysDeptDTO> listUserDeptWithChildren(SysUserDeptParameter parameter) {
        return this.localSysUserApi.listUserDeptWithChildren(parameter);
    }

    /**
     * 查询用户第三方账号列表
     *
     * @param parameter 参数
     * @return 用户第三方账号列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER_THIRD_ACCOUNT)
    public List<SysUserThirdAccountDTO> listUserThirdAccount(@RequestBody @Valid SysUserThirdAccountParameter parameter) {
        return this.localSysUserApi.listUserThirdAccount(parameter);
    }
}
