package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysTenantApi;
import com.smart.module.api.system.dto.SysTenantDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shizhongming
 * 2025/6/15 20:50
 * @since 5.0.0
 */
@Component
public class RemoteSysTenantApiFallback implements RemoteSysTenantApi {
    @Override
    public List<SysTenantDTO> listTenantById(List<Long> tenantIdList) {
        return List.of();
    }

    @Override
    public List<SysTenantDTO> listTenantByCode(List<String> tenantCodeList) {
        return List.of();
    }
}
