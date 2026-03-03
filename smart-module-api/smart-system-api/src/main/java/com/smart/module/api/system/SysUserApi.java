package com.smart.module.api.system;

import com.smart.module.api.system.dto.*;
import com.smart.module.api.system.parameter.RemoteSysUserListParameter;
import com.smart.module.api.system.parameter.SysUserDeptParameter;
import com.smart.module.api.system.parameter.SysUserThirdAccountParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

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
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    default SysUserDTO getUserByUsername(String username) {
        List<SysUserDTO> userList = listUserByUsername(List.of(username));
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        return userList.getFirst();
    }


    /**
     * 通过ID查询用户
     * @param userIdList 用户ID列表
     * @return List
     */
    List<SysUserDTO> listUserById(List<Long> userIdList);

    /**
     * 通过ID查询用户
     * @param userId 用户ID
     * @return 用户
     */
    default SysUserDTO getUserById(Long userId) {
        List<SysUserDTO> userList = listUserById(List.of(userId));
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        return userList.getFirst();
    }

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

    /**
     * 查询用户列表
     * @param parameter 参数
     * @return 用户列表
     */
    List<SysUserDTO> listUser(RemoteSysUserListParameter parameter);

    /**
     * 查询用户部门列表
     * @param parameter 参数
     * @return 用户部门列表
     */
    List<SysDeptDTO> listUserDept(@Nullable SysUserDeptParameter parameter);

    /**
     * 查询用户部门及子部门列表
     * @param parameter 参数
     * @return 用户部门及子部门列表
     */
    List<SysDeptDTO> listUserDeptWithChildren(SysUserDeptParameter parameter);

    /**
     * 查询用户第三方账号列表
     * @param parameter 参数
     * @return 用户第三方账号列表
     */
    List<SysUserThirdAccountDTO> listUserThirdAccount(SysUserThirdAccountParameter parameter);
}
