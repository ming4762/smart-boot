package com.smart.commons.jwt.exception;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2022/8/8
 * @since 3.0.0
 */
public class JwtException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2648792509314004892L;

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}
