package com.smart.module.api.system;

import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;

import java.util.List;

/**
 * 系统用户接口
 * @author zhongming4762
 * 2023/3/11
 */
public interface SysUserApi {

    /**
     * 通过用户名查询用户
     * @param usernameList 用户名列表
     * @return List
     */
    List<SysUserDTO> listUserByUsername(List<String> usernameList);

    /**
     * 通过ID查询用户
     * @param userIdList 用户ID列表
     * @return List
     */
    List<SysUserDTO> listUserById(List<Long> userIdList);

    /**
     * 锁定账户
     * @param parameter 参数
     * @return 账户锁定参数
     */
    boolean lockAccount(UserAccountLockDTO parameter);

    /**
     * 重置登录失败次数
     * @param parameter 参数
     * @return 是否成功
     */
    boolean updateLoginFailTime(AccountLoginFailTimeUpdateDTO parameter);
}
