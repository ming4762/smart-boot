package com.smart.module.system.pojo.vo.parameter;

import com.smart.module.api.system.dto.SysTenantDTO;
import com.smart.module.system.inject.SysTenantInject;
import com.smart.module.system.model.SysParameterTenantPO;
import lombok.Getter;

/**
 *
 * @author shizhongming
 * 2025/9/5 15:37
 * @since 5.0.0
 */
@Getter
public class SysParameterTenantListVO extends SysParameterTenantPO implements SysTenantInject {

    private SysTenantDTO tenant;

    /**
     * 设置租户
     *
     * @param tenant 租户
     */
    @Override
    public void setTenant(SysTenantDTO tenant) {
        this.tenant = tenant;
    }
}
