package com.smart.framework.auth.extensions.wechat.authentication;

import com.smart.framework.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import com.smart.framework.auth.core.constants.AuthTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * 微信登录认证token
 * @author zhongming4762
 * 2023/4/3
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class WechatAuthenticationToken extends AbstractEnhanceAuthenticationToken {

    @Serial
    private static final long serialVersionUID = -6243856978789980764L;
    private final String appid;

    public WechatAuthenticationToken(AuthTypeEnum authType, String appid, String credentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authType, credentials, principal, authorities);
        this.appid = appid;
    }

    public WechatAuthenticationToken(AuthTypeEnum authType, String appid, String credentials) {
        this(authType, appid, credentials, null, null);
    }
}
