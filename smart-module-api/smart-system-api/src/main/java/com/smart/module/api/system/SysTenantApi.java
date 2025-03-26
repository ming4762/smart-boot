package com.smart.module.api.system;

import com.smart.module.api.system.dto.SysTenantDTO;

import java.util.List;

/**
 * 租户接口
 * @author shizhongming
 * 2025/3/26 17:52
 * @since 5.0.0
 */
public interface SysTenantApi {

    /**
     * 通过租户id查询租户信息
     * @param tenantIdList 租户id列表
     * @return 租户信息
     */
    List<SysTenantDTO> listTenantById(List<Long> tenantIdList);

    /**
     * 通过租户编号查询租户信息
     * @param tenantCodeList 租户编号列表
     * @return 租户信息
     */
    List<SysTenantDTO> listTenantByCode(List<String> tenantCodeList);
}
