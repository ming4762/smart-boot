package com.smart.module.system.inject;

import com.smart.module.api.system.dto.SysTenantDTO;

/**
 * 注入租户信息接口
 * @author shizhongming
 * 2024/5/7 20:22
 * @since 3.0.0
 */
public interface SysTenantInject {

    /**
     * 获取租户ID
     * @return 租户ID
     */
    Long getTenantId();

    /**
     * 设置租户
     * @param tenant 租户
     */
    void setTenant(SysTenantDTO tenant);
}
