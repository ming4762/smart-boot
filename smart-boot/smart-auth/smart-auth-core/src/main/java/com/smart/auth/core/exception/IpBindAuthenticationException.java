package com.smart.auth.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * IP绑定认证异常
 * @author ShiZhongMing
 * 2021/12/29
 * @since 1.0.7
 */
public class IpBindAuthenticationException extends AuthenticationException {
    private static final long serialVersionUID = -4481564366185413211L;

    public IpBindAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IpBindAuthenticationException(String msg) {
        super(msg);
    }
}
