package com.smart.module.api.system.dto;

import com.smart.module.api.system.constants.MaxConnectionsPolicyEnum;
import com.smart.module.api.system.constants.UserAccountStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户账户DTO
 * @author zhongming4762
 * 2023/3/11
 */
@Getter
@Setter
@ToString
public class SysUserAccountDTO implements Serializable {

    private Long userId;

    /**
     * 登录失败次数
     */
    private Long loginFailTime;

    private UserAccountStatusEnum accountStatus;

    private Boolean initialPasswordYn;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    private String ipWhiteList;

    private Long maxConnections;

    private Long maxDaysSinceLogin;

    private Long passwordLifeDays;

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
