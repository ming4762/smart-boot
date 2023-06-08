package com.smart.auth.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.LockedException;

import java.io.Serial;

/**
 * 长时间未登录锁定异常
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0.0
 */
public class LongTimeNoLoginLockedException extends LockedException {
    @Serial
    private static final long serialVersionUID = 3379836981811133440L;

    @Getter
    private final User user;

    public LongTimeNoLoginLockedException(String msg, User user) {
        super(msg);
        this.user = user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {
        private final Long userId;

        private final String username;

        private final String fullName;
    }
}
