package com.smart.module.api.system.dto;

import com.smart.module.api.system.constants.MaxConnectionsPolicyEnum;
import com.smart.module.api.system.constants.UserAccountStatusEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录查询结果
 * @author zhongming4762
 * 2023/6/7
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO implements Serializable {

    /**
     * 用户信息
     */
    private Long userId;

    private String username;

    private String fullName;

    private String mobile;

    private String password;

    private UserAccountDTO account;

    /**
     * 账户信息
     */
    @Getter
    @Setter
    @ToString
    public static class UserAccountDTO implements Serializable {
        private Long loginFailTime;

        private UserAccountStatusEnum accountStatus;
        /**
         * 上次登录时间
         */
        private LocalDateTime lastLoginTime;
        private Long maxConnections;

        private Long maxDaysSinceLogin;
        private Long passwordLifeDays;
        private String ipWhiteList;

        /**
         * 超出最大连接数执行策略
         */
        private MaxConnectionsPolicyEnum maxConnectionsPolicy;

        /**
         * 登录失败锁定次数，0永不锁定
         */
        private Long loginFailTimeLimit;

        /**
         * 密码修改时间
         */
        private LocalDateTime passwordModifyTime;

        private Long passwordErrorUnlockSecond;

        /**
         * 账户锁定时间
         */
        private LocalDateTime lockTime;
    }
}
