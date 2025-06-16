package com.smart.cloud.api.system.feign;

import com.smart.cloud.api.system.feign.fallback.RemoteSysTenantApiFallback;
import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.module.api.system.SysTenantApi;
import com.smart.module.api.system.constants.SystemApiUrlConstants;
import com.smart.module.api.system.dto.SysTenantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author shizhongming
 * 2025/3/26 18:01
 * @since 5.0.0
 */
@FeignClient(value = CloudServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSysTenantApiFallback.class, contextId = "remoteSysTenantApi")
public interface RemoteSysTenantApi extends SysTenantApi {

    /**
     * 通过租户id查询租户信息
     *
     * @param tenantIdList 租户id列表
     * @return 租户信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TENANT_LIST_BY_ID)
    List<SysTenantDTO> listTenantById(List<Long> tenantIdList);

    /**
     * 通过租户编号查询租户信息
     *
     * @param tenantCodeList 租户编号列表
     * @return 租户信息
     */
    @Override
    @PostMapping(SystemApiUrlConstants.TENANT_LIST_BY_CODE)
    List<SysTenantDTO> listTenantByCode(List<String> tenantCodeList);
}
