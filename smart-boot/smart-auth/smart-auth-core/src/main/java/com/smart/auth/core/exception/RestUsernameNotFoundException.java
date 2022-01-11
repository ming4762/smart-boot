package com.smart.auth.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户名不存在异常
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
public class RestUsernameNotFoundException extends AuthenticationException {
    private static final long serialVersionUID = -8581793774073773018L;

    public RestUsernameNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RestUsernameNotFoundException(String msg) {
        super(msg);
    }
}
