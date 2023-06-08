package com.smart.cloud.api.system.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 系统模块认证用户远程调用接口
 * @author zhongming4762
 * 2023/3/8
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, contextId = "remoteSystemAuthUserApi")
public interface RemoteSystemAuthUserApi extends SystemAuthUserApi {

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Nullable
    @Override
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_USERNAME)
    AuthUserDTO getByUsername(@NonNull String username);

    /**
     * 通过手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Nullable
    @Override
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_PHONE)
    AuthUserDTO getByMobile(@NonNull String mobile);

    /**
     * 查询用户角色权限信息
     *
     * @param userId 用户ID
     * @return 权限角色信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.QUERY_ROLE_PERMISSION)
    UserRolePermission queryRolePermission(@NonNull Long userId);

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_OPENID)
    AuthUserDTO getByAppOpenid(WechatUserQueryParameter parameter);

    /**
     * 通过unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_UNIONID)
    AuthUserDTO getByUnionid(WechatUserQueryParameter parameter);

    /**
     * 结果用户账户
     *
     * @param parameter 参数
     * @return 是否结果成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.USER_ACCOUNT_UNLOCK)
    boolean unlockAccount(UserAccountUnLockParameter parameter);
}
