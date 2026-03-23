package com.smart.module.system.api.remote;

import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.DingtalkUserQueryParameter;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import com.smart.module.system.service.impl.LocalSystemAuthUserApiImpl;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * 系统模块服务间调用接口
 * @author zhongming4762
 * 2023/3/8
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RemoteSystemAuthUserApiController implements SystemAuthUserApi {

    private final LocalSystemAuthUserApiImpl localSystemAuthUserApi;

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_USERNAME)
    @Override
    public AuthUserDTO getByUsername(@NonNull @RequestParam("username") String username) {
        return this.localSystemAuthUserApi.getByUsername(username);
    }

    /**
     * 通过电话查询用户
     * @param mobile 电话
     * @return 用户信息
     */
    @PostMapping(SystemApiUrlConstants.GET_AUTH_USER_BY_PHONE)
    @Override
    public AuthUserDTO getByMobile(@NonNull@RequestBody String mobile) {
        return this.localSystemAuthUserApi.getByMobile(mobile);
    }

    /**
     * 查询用户角色权限信息
     * @param parameter 参数
     * @return 权限角色信息
     */
    @PostMapping(SystemApiUrlConstants.QUERY_ROLE_PERMISSION)
    @Override
    public UserAccountData queryUserAccount(@NonNull @RequestBody QueryUserAccountDTO parameter) {
        return this.localSystemAuthUserApi.queryUserAccount(parameter);
    }

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_OPENID)
    public AuthUserDTO getByWehchatAppOpenid(@RequestBody WechatUserQueryParameter parameter) {
        return SystemAuthUserApi.super.getByWehchatAppOpenid(parameter);
    }

    /**
     * 通过unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.WECHAT_GET_BY_APP_UNIONID)
    public AuthUserDTO getByWechatUnionid(@RequestBody WechatUserQueryParameter parameter) {
        return SystemAuthUserApi.super.getByWechatUnionid(parameter);
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
        return this.localSystemAuthUserApi.unlockAccount(parameter);
    }

    /**
     * 通过钉钉openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
     @PostMapping(SystemApiUrlConstants.DINGTALK_GET_USER_BY_APP_OPENID)
    public AuthUserDTO getByDingtalkOpenId(@RequestBody DingtalkUserQueryParameter parameter) {
        return this.localSystemAuthUserApi.getByDingtalkOpenId(parameter);
    }

    /**
     * 通过钉钉unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DINGTALK_GET_USER_BY_APP_UNIONID)
    public AuthUserDTO getByDingtalkUnionId(@RequestBody DingtalkUserQueryParameter parameter) {
        return this.localSystemAuthUserApi.getByDingtalkUnionId(parameter);
    }

    /**
     * 通过钉钉手机号获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    @PostMapping(SystemApiUrlConstants.DINGTALK_GET_USER_BY_APP_MOBILE)
    public AuthUserDTO getByDingtalkMobile(@RequestBody DingtalkUserQueryParameter parameter) {
        return this.localSystemAuthUserApi.getByDingtalkMobile(parameter);
    }
}
