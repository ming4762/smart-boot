package com.smart.auth.core.exception;

import org.springframework.security.authentication.LockedException;

/**
 * 长时间未登录锁定异常
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0.0
 */
public class LongTimeNoLoginLockedException extends LockedException {
    private static final long serialVersionUID = 3379836981811133440L;

    public LongTimeNoLoginLockedException(String msg) {
        super(msg);
    }
}
