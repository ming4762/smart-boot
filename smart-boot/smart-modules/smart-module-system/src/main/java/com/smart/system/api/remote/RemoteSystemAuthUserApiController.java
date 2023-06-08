package com.smart.system.api.remote;

import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import com.smart.system.service.impl.LocalSystemAuthUserApiImpl;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统模块服务间调用接口
 * @author zhongming4762
 * 2023/3/8
 */
@RestController
@RequestMapping
public class RemoteSystemAuthUserApiController implements SystemAuthUserApi {

    private final LocalSystemAuthUserApiImpl systemAuthUserApi;

    public RemoteSystemAuthUserApiController(LocalSystemAuthUserApiImpl systemAuthUserApi) {
        this.systemAuthUserApi = systemAuthUserApi;
    }

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_USERNAME)
    @Override
    public AuthUserDTO getByUsername(@NonNull @RequestBody String username) {
        return this.systemAuthUserApi.getByUsername(username);
    }

    /**
     * 通过电话查询用户
     * @param mobile 电话
     * @return 用户信息
     */
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_PHONE)
    @Override
    public AuthUserDTO getByMobile(@NonNull@RequestBody String mobile) {
        return this.systemAuthUserApi.getByMobile(mobile);
    }

    /**
     * 查询用户角色权限信息
     * @param userId 用户ID
     * @return 权限角色信息
     */
    @PostMapping(SystemApiUrlConstants.QUERY_ROLE_PERMISSION)
    @Override
    public UserRolePermission queryRolePermission(@NonNull @RequestBody Long userId) {
        return this.systemAuthUserApi.queryRolePermission(userId);
    }

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_OPENID)
    public AuthUserDTO getByAppOpenid(WechatUserQueryParameter parameter) {
        return SystemAuthUserApi.super.getByAppOpenid(parameter);
    }

    /**
     * 通过unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_UNIONID)
    public AuthUserDTO getByUnionid(WechatUserQueryParameter parameter) {
        return SystemAuthUserApi.super.getByUnionid(parameter);
    }

    /**
     * 结果用户账户
     *
     * @param parameter 参数
     * @return 是否结果成功
     */
    @Override
    @PostMapping(SystemApiUrlConstants.USER_ACCOUNT_UNLOCK)
    public boolean unlockAccount(@RequestBody UserAccountUnLockParameter parameter) {
        return this.systemAuthUserApi.unlockAccount(parameter);
    }
}
