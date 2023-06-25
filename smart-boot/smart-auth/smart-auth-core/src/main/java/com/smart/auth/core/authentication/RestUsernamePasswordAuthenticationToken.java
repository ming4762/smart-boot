package com.smart.auth.core.authentication;

import com.smart.auth.core.constants.LoginTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class RestUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Boolean bindIp;

    private final String loginIp;

    private final LoginTypeEnum loginType;

    public RestUsernamePasswordAuthenticationToken(Object principal, Object credentials, Boolean bindIp, String loginIp, LoginTypeEnum loginType) {
        super(principal, credentials);
        this.bindIp = bindIp;
        this.loginIp = loginIp;
        this.loginType = loginType;
    }

    public RestUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Boolean bindIp, String loginIp, LoginTypeEnum loginType) {
        super(principal, credentials, authorities);
        this.bindIp = bindIp;
        this.loginIp = loginIp;
        this.loginType = loginType;
    }
}
