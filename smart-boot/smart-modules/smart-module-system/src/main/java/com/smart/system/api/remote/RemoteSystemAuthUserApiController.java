package com.smart.system.api.remote;

import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AuthUser;
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
    public AuthUser getByUsername(@NonNull @RequestBody String username) {
        return this.systemAuthUserApi.getByUsername(username);
    }

    /**
     * 通过电话查询用户
     * @param phone 电话
     * @return 用户信息
     */
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_PHONE)
    @Override
    public AuthUser getByPhone(@NonNull@RequestBody String phone) {
        return this.systemAuthUserApi.getByPhone(phone);
    }

    /**
     * 查询用户角色权限信息
     * @param authUser 用户信息
     * @return 权限角色信息
     */
    @PostMapping(SystemApiUrlConstants.QUERY_ROLE_PERMISSION)
    @Override
    public UserRolePermission queryRolePermission(@NonNull @RequestBody AuthUser authUser) {
        return this.systemAuthUserApi.queryRolePermission(authUser);
    }

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_OPENID)
    public AuthUser getByAppOpenid(WechatUserQueryParameter parameter) {
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
    public AuthUser getByUnionid(WechatUserQueryParameter parameter) {
        return SystemAuthUserApi.super.getByUnionid(parameter);
    }
}
