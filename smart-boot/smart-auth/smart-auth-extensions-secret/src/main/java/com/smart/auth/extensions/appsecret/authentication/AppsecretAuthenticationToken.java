package com.smart.auth.extensions.appsecret.authentication;

import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

/**
 * app secret 登录token
 * @author ShiZhongMing
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
public class AppsecretAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = -3223772613702203952L;

    private final Serializable principal;

    private final Serializable credentials;

    private final String ip;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public AppsecretAuthenticationToken(Serializable principal, Serializable credentials, String ip, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.ip = ip;
    }

    public AppsecretAuthenticationToken(Serializable principal, Serializable credentials, String ip) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.ip = ip;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getIp() {
        return this.ip;
    }
}
