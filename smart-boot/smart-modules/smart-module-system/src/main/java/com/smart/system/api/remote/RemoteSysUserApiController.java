package com.smart.system.api.remote;

import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import com.smart.system.api.local.LocalSysUserApi;
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
}
