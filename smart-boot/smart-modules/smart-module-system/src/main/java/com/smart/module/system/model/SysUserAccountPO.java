package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.commons.core.dto.auth.MaxConnectionsPolicyEnum;
import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import com.smart.module.system.mybatis.type.MaxConnectionsPolicyTypeHandler;
import com.smart.module.system.mybatis.type.UserAccountStatusTypeHandler;
import lombok.*;

import java.io.Serial;
import java.time.ZonedDateTime;

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
public class SysUserAccountPO extends BaseModelCreateUserTime {

    @Serial
    private static final long serialVersionUID = -8246582845593471040L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /**
     * 登录失败次数
     */
    private Long loginFailTime;

    @TableField(typeHandler = UserAccountStatusTypeHandler.class)
    private UserAccountStatusEnum accountStatus;

    private Boolean initialPasswordYn;

    /**
     * 上次登录时间
     */
    private ZonedDateTime lastLoginTime;

    private String ipWhiteList;

    private Long maxConnections;

    private Long maxDaysSinceLogin;

    private Long passwordLifeDays;

    /**
     * 超出最大连接数执行策略
     */
    @TableField(typeHandler = MaxConnectionsPolicyTypeHandler.class)
    private MaxConnectionsPolicyEnum maxConnectionsPolicy;

    /**
     * 登录失败锁定次数，0永不锁定
     */
    private Long loginFailTimeLimit;

    /**
     * 密码修改时间
     */
    private ZonedDateTime passwordModifyTime;

    private Long passwordErrorUnlockSecond;

    /**
     * 账户锁定时间
     */
    private ZonedDateTime lockTime;

    private Long tenantId;

}
