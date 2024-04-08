package com.smart.system.pojo.vo.tenant;

import com.smart.system.model.tenant.SysTenantPackagePO;
import com.smart.system.model.tenant.SysTenantSubscribePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 租户订阅查询VO
 * @author shizhongming
 * 2024/4/8 15:49
 * @since 3.0.0
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SysTenantSubscribeListVO extends SysTenantSubscribePO {

    @Serial
    private static final long serialVersionUID = -2927944100385184890L;
    /**
     * 套餐包信息
     */
    private SysTenantPackagePO tenantPackage;
}
