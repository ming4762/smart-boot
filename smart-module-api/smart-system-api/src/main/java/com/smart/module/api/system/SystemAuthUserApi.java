package com.smart.module.api.system;

import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.DingtalkUserQueryParameter;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;

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
     * @param parameter 参数
     * @return 账户信息
     */
    UserAccountData queryUserAccount(@NonNull QueryUserAccountDTO parameter);

    /**
     * 通过openid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByWehchatAppOpenid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 通过unionid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByWechatUnionid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 结果用户账户
     * @param parameter 参数
     * @return 是否结果成功
     */
    boolean unlockAccount(UserAccountUnLockParameter parameter);

     /**
     * 通过钉钉openid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByDingtalkOpenId(DingtalkUserQueryParameter parameter) {
        return null;
    }

     /**
     * 通过钉钉unionid获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByDingtalkUnionId(DingtalkUserQueryParameter parameter) {
        return null;
    }

     /**
     * 通过钉钉手机号获取用户信息
     * @param parameter 参数
     * @return AuthUser
     */
    default AuthUserDTO getByDingtalkMobile(DingtalkUserQueryParameter parameter) {
        return null;
    }
}
