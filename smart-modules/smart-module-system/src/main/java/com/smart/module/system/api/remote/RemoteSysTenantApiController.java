package com.smart.module.system.api.remote;

import com.smart.module.api.system.SysTenantApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysTenantDTO;
import com.smart.module.system.api.local.LocalSysTenantApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author shizhongming
 * 2025/3/26 18:00
 * @since 5.0.0
 */
@RequiredArgsConstructor
@Component
public class RemoteSysTenantApiController implements SysTenantApi {

    private final LocalSysTenantApi sysTenantApi;

    /**
     * 通过租户id查询租户信息
     *
     * @param tenantIdList 租户id列表
     * @return 租户信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TENANT_LIST_BY_ID)
    public List<SysTenantDTO> listTenantById(@RequestBody List<Long> tenantIdList) {
        return this.sysTenantApi.listTenantById(tenantIdList);
    }

    /**
     * 通过租户编号查询租户信息
     *
     * @param tenantCodeList 租户编号列表
     * @return 租户信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TENANT_LIST_BY_CODE)
    public List<SysTenantDTO> listTenantByCode(@RequestBody List<String> tenantCodeList) {
        return this.sysTenantApi.listTenantByCode(tenantCodeList);
    }
}
