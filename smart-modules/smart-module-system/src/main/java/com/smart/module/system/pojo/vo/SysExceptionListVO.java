package com.smart.module.system.pojo.vo;

import com.smart.module.system.model.SysExceptionPO;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/2/16 15:09
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysExceptionListVO extends SysExceptionPO {
    @Serial
    private static final long serialVersionUID = 7297923753641156750L;

    private SysUserPO resolvedUser;

    /**
     * 租户信息
     */
    private SysTenantPO tenant;
}
