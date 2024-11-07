package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSysUserApi")
public interface RemoteSysUserApi extends SysUserApi {

    /**
     * 通过用户名查询用户
     *
     * @param usernameList 用户名列表
     * @return List
     */
    @Override
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_USERNAME)
    List<SysUserDTO> listUserByUsername(List<String> usernameList);

    /**
     * 通过ID查询用户
     *
     * @param userIdList 用户ID列表
     * @return List
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER_BY_ID)
    List<SysUserDTO> listUserById(List<Long> userIdList);

    /**
     * 锁定账户
     *
     * @param parameter 参数
     * @return 账户锁定参数
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LOCK_ACCOUNT)
    boolean lockAccount(UserAccountLockDTO parameter);

    /**
     * 重置登录失败次数
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.RESET_LOGIN_FAIL_TIME)
    boolean updateLoginFailTime(AccountLoginFailTimeUpdateDTO userId);

    /**
     * 查询用户列表
     *
     * @param parameter 参数
     * @return 用户列表
     */
    @Override
    @PostMapping(SystemApiUrlConstants.LIST_USER)
    List<SysUserDTO> listUser(RemoteSysUserListParameter parameter);
}
