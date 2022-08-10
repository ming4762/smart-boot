package com.smart.system.pojo.dbo;

import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysUserPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author ShiZhongMing
 * 2022/1/26 17:20
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class SysUserWthAccountBO extends SysUserPO {

    @Serial
    private static final long serialVersionUID = -314839324747456457L;
    /**
     * 登录失败次数
     */
    private Integer loginFailTime;

    private UserAccountStatusEnum accountStatus;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    private Boolean initialPasswordYn;
}
