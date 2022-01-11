package com.smart.auth.extensions.sms.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * SMS认证信息
 * @author ShiZhongMing
 * 2021/6/3 13:14
 * @since 1.0
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class SmsAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -5507214988425089305L;

    /**
     * 登录的手机号
     */
    private final Serializable principal;

    /**
     * 验证码
     */
    private final Serializable credentials;

    public SmsAuthenticationToken(Serializable principal, Serializable credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
    }

    public SmsAuthenticationToken(Serializable principal, Serializable credentials,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
