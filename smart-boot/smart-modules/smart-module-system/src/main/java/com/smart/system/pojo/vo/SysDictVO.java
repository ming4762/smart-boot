package com.smart.system.pojo.vo;

import com.smart.system.model.SysDictPO;
import com.smart.system.model.tenant.SysTenantPO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/4/15 20:37
 * @since 3.0.0
 */
@Getter
@Setter
public class SysDictVO extends SysDictPO {
    @Serial
    private static final long serialVersionUID = -4015144224686062542L;

    private SysTenantPO tenant;
}
