package com.smart.auth.core.exception;

import org.springframework.security.authentication.LockedException;

/**
 * 密码长时间未修改锁定
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0.0
 */
public class PasswordNoLifeLockedException extends LockedException {
    private static final long serialVersionUID = -8176382060796649363L;

    public PasswordNoLifeLockedException(String msg) {
        super(msg);
    }
}
