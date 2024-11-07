package com.smart.framework.auth.core.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * 登录信息缺失异常
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0
 */
public class LoginInfoMissAuthenticationException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 76782667254371064L;

    public LoginInfoMissAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LoginInfoMissAuthenticationException(String msg) {
        super(msg);
    }
}
