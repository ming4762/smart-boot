package com.smart.auth.core.userdetails;

import com.smart.auth.core.constants.AuthTypeEnum;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhongming4762
 * 2023/4/4
 */
public interface WechatUserDetailService {

    /**
     * 通过微信openid加载用户信息
     * @param openid openid
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    default RestUserDetails loadUserByOpenid(AuthTypeEnum authType, String appid, String openid) throws AuthenticationException {
        return null;
    }

    /**
     * 通过微信unionid加载用户信息
     * @param unionid unionid
     * @return 用户信息
     * @throws AuthenticationException 异常信息
     */
    default RestUserDetails loadUserByUnionid(AuthTypeEnum authType, String appid, String unionid) throws AuthenticationException {
        return null;
    }
}
