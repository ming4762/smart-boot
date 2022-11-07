package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import com.smart.system.constants.MaxConnectionsPolicyEnum;
import com.smart.system.constants.UserAccountStatusEnum;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 用户认证信息
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0.7
 */
@Getter
@Setter
@TableName(value = "sys_user_account", autoResultMap = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysUserAccountPO extends BaseModel {
    @Serial
    private static final long serialVersionUID = -8246582845593471040L;

    @TableId
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

    private LocalDateTime createTime;

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
