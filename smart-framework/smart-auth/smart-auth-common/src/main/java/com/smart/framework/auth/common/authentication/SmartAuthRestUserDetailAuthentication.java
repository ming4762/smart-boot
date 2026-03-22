package com.smart.framework.auth.common.authentication;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 存储用户信息的Authentication
 * 不用于认证
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 00:19
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartAuthRestUserDetailAuthentication implements Authentication {

    private final RestUserDetails restUserDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return restUserDetails.getAuthorities();
    }


    @Override
    public Object getCredentials() {
        return null;
    }


    @Override
    public Object getDetails() {
        throw new UnsupportedOperationException("getDetails");
    }


    @Override
    public Object getPrincipal() {
        return this.restUserDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("setAuthenticated");
    }


    @Override
    public String getName() {
        return this.restUserDetails.getUsername();
    }
}
