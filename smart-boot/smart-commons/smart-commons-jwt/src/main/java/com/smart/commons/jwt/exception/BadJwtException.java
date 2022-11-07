package com.smart.commons.jwt.exception;

/**
 * @author ShiZhongMing
 * 2022/8/9
 * @since 3.0.0
 */
public class BadJwtException extends JwtException {
    private static final long serialVersionUID = -8488676614475760852L;

    public BadJwtException(String message) {
        super(message);
    }

    public BadJwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
