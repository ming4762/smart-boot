package com.smart.auth.core.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * 验证码过期异常
 * @author ShiZhongMing
 * 2021/6/2
 * @since 1.0
 */
@Getter
public class CaptchaException extends AuthenticationException {
    private static final long serialVersionUID = -7896913588929601365L;

    public CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaException(String msg) {
        super(msg);
    }

}
