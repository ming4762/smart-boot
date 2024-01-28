package com.smart.auth.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.LockedException;

import java.io.Serial;
import java.io.Serializable;

/**
 * 长时间未登录锁定异常
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0.0
 */
@Getter
public class LongTimeNoLoginLockedException extends LockedException {
    @Serial
    private static final long serialVersionUID = 3379836981811133440L;

    private final User user;

    public LongTimeNoLoginLockedException(String msg, User user) {
        super(msg);
        this.user = user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = 3784460416887835637L;
        private final Long userId;

        private final String username;

        private final String fullName;
    }
}
