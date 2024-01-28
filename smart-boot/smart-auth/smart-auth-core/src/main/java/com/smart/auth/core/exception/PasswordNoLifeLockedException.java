package com.smart.auth.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.LockedException;

import java.io.Serial;
import java.io.Serializable;

/**
 * 密码长时间未修改锁定
 * @author ShiZhongMing
 * 2022/4/2
 * @since 2.0.0
 */
@Getter
public class PasswordNoLifeLockedException extends LockedException {
    @Serial
    private static final long serialVersionUID = -8176382060796649363L;

    private final User user;

    public PasswordNoLifeLockedException(String msg, User user) {
        super(msg);
        this.user = user;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User implements Serializable {
        private final Long userId;

        private final String username;

        private final String fullName;
    }
}
