package com.smart.module.system.pojo.vo;

import com.smart.framework.crud.model.BaseUser;
import com.smart.framework.crud.model.CreateUserSetter;
import com.smart.module.system.model.SysLogPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2022/1/20 15:15
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class SysLogListVO extends SysLogPO implements CreateUserSetter {

    @Serial
    private static final long serialVersionUID = 8493022723816142778L;
    private BaseUser createUser;

    /**
     * 租户信息
     */
    private SysTenantPO tenant;
}
