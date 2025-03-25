package com.smart.framework.commons.jwt.exception;

/**
 * JWT过期异常
 * @author shizhongming
 * 2025/3/25 11:00
 * @since 5.0.0
 */
public class JwtExpiredException extends JwtException {

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
