package com.smart.captcha.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * 验证码异常
 * @author ShiZhongMing
 * 2021/6/2
 * @since 1.0
 */
@Getter
public class CaptchaException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -7896913588929601365L;

    public CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaException(String msg) {
        super(msg);
    }

}
