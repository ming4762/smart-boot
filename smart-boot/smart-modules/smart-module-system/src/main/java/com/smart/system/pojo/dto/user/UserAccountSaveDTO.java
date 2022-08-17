package com.smart.system.pojo.dto.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.smart.system.constants.MaxConnectionsPolicyEnum;
import com.smart.system.mybatis.type.MaxConnectionsPolicyTypeHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 保存用户账户信息DTO
 * @author ShiZhongMing
 * 2022/8/17
 * @since 3.0.0
 */
@ToString
@Getter
@Setter
public class UserAccountSaveDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3760196266685002568L;

    private Long userId;

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

    private Long passwordErrorUnlockSecond;
}
