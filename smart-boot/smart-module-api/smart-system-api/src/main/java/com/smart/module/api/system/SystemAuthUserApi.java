package com.smart.module.api.system;

import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * 获取认证用户的API
 * @author zhongming4762
 * 2023/3/8
 */
public interface SystemAuthUserApi {

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Nullable
    default AuthUserDTO getByUsername(@NonNull String username) {
        return null;
    }

    /**
     * 通过手机号查询用户
     * @param mobile 手机号
     * @return 用户信息
     */
    @Nullable
    default AuthUserDTO getByMobile(@NonNull String mobile) {
        return null;
    }

    /**
     * 查询用户角色权限信息
     * @param userId 用户ID
     * @return 权限角色信息
     */
    UserRolePermission queryRolePermission(@NonNull Long userId);

    /**
     * 通过openid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByAppOpenid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 通过unionid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByUnionid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 结果用户账户
     * @param parameter 参数
     * @return 是否结果成功
     */
    boolean unlockAccount(UserAccountUnLockParameter parameter);
}
