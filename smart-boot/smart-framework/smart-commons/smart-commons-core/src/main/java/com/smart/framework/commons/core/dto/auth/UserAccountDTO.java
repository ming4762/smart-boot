package com.smart.framework.commons.core.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账户信息
 * @author shizhongming
 * 2024/4/8 18:50
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class UserAccountDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5049297174154462028L;
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
