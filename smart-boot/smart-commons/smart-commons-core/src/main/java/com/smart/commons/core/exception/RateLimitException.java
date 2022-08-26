package com.smart.commons.core.exception;

import java.io.Serial;

/**
 * 限流异常
 * @author ShiZhongMing
 * 2022/8/25
 * @since 3.0.0
 */
public class RateLimitException extends BusinessException {
    @Serial
    private static final long serialVersionUID = -117624898470009446L;

    public RateLimitException(String message) {
        super(message);
    }
}
