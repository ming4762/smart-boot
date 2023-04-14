package com.smart.auth.extensions.wechat.authentication;

import com.smart.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import com.smart.auth.core.constants.AuthTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信登录认证token
 * @author zhongming4762
 * 2023/4/3
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class WechatAuthenticationToken extends AbstractEnhanceAuthenticationToken {

    private final String appid;

    public WechatAuthenticationToken(AuthTypeEnum authType, String appid, String credentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authType, credentials, principal, authorities);
        this.appid = appid;
    }

    public WechatAuthenticationToken(AuthTypeEnum authType, String appid, String credentials) {
        this(authType, appid, credentials, null, null);
    }
}
