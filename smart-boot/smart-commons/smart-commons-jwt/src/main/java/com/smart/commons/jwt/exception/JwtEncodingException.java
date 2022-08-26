package com.smart.commons.jwt.exception;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2022/8/8 17:02
 * @since 1.0
 */
public class JwtEncodingException extends JwtException {
    @Serial
    private static final long serialVersionUID = 5015322190214803194L;

    public JwtEncodingException(String message) {
        super(message);
    }

    public JwtEncodingException(String message, Throwable cause) {
        super(message, cause);
    }
}