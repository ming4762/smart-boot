package com.smart.framework.auth.extensions.wechat.userdetails;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.userdetails.UserDetailsBuilder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhongming4762
 * 2023/6/7
 */
@RequiredArgsConstructor
public class DefaultWechatUserDetailServiceImpl implements WechatUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;
    private final UserDetailsBuilder userDetailsBuilder;

    /**
     * 通过微信openid加载用户信息
     *
     * @param authType 登录方式
     * @param appid appid
     * @param openid   openid
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    @Override
    public RestUserDetails loadUserByAppOpenid(AuthTypeEnum authType, String appid, String openid) throws AuthenticationException {
        AuthUserDTO user = this.systemAuthUserApi.getByWehchatAppOpenid(
                WechatUserQueryParameter.builder()
                        .appid(appid)
                        .openid(openid)
                        .build()
        );
        return userDetailsBuilder.buildUserDetails(user);
    }

    /**
     * 通过微信unionid加载用户信息
     *
     * @param authType 登录方式
     * @param appid appid
     * @param unionid  unionid
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    @Override
    public RestUserDetails loadUserByUnionid(AuthTypeEnum authType, String appid, String unionid) throws AuthenticationException {
        AuthUserDTO user = this.systemAuthUserApi.getByWechatUnionid(
                WechatUserQueryParameter.builder()
                        .appid(appid)
                        .unionid(unionid)
                        .build()
        );
        return userDetailsBuilder.buildUserDetails(user);
    }
}
