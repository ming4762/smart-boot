package com.smart.auth.extensions.wechat.userdetails;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.token.TokenRepository;
import com.smart.auth.core.userdetails.AbstractUserDetailsService;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/6/7
 */
public class DefaultWechatUserDetailServiceImpl extends AbstractUserDetailsService implements WechatUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;

    protected DefaultWechatUserDetailServiceImpl(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        super(tokenRepositoryList, systemAuthUserApi);
        this.systemAuthUserApi = systemAuthUserApi;
    }

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
        AuthUserDTO user = this.systemAuthUserApi.getByAppOpenid(
                WechatUserQueryParameter.builder()
                        .appid(appid)
                        .openid(openid)
                        .build()
        );
        return this.getUserDetails(user);
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
        AuthUserDTO user = this.systemAuthUserApi.getByUnionid(
                WechatUserQueryParameter.builder()
                        .appid(appid)
                        .unionid(unionid)
                        .build()
        );
        return this.getUserDetails(user);
    }
}
