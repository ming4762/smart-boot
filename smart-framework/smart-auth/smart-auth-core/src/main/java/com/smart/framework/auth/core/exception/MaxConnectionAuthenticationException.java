package com.smart.framework.auth.core.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * 最大连接数异常
 * @author ShiZhongMing
 * 2022/4/13
 * @since 2.0.0
 */
public class MaxConnectionAuthenticationException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = -2100929945809006686L;

    public MaxConnectionAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MaxConnectionAuthenticationException(String msg) {
        super(msg);
    }
}
