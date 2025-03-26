package com.smart.module.system.api.local;

import com.smart.module.api.system.SysTenantApi;
import com.smart.module.api.system.dto.SysTenantDTO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 租户接口
 * @author shizhongming
 * 2025/3/26 17:56
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
@Primary
public class LocalSysTenantApi implements SysTenantApi {

    private final SysTenantService sysTenantService;

    /**
     * 通过租户id查询租户信息
     *
     * @param tenantIdList 租户id列表
     * @return 租户信息
     */
    @Override
    public List<SysTenantDTO> listTenantById(List<Long> tenantIdList) {
        if (CollectionUtils.isEmpty(tenantIdList)) {
            return Collections.emptyList();
        }
        return sysTenantService.lambdaQuery()
                .in(SysTenantPO::getId, tenantIdList)
                .eq(SysTenantPO::getUseYn, Boolean.TRUE)
                .list().stream()
                .map(item -> {
                    SysTenantDTO dto = new SysTenantDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }

    /**
     * 通过租户编号查询租户信息
     *
     * @param tenantCodeList 租户编号列表
     * @return 租户信息
     */
    @Override
    public List<SysTenantDTO> listTenantByCode(List<String> tenantCodeList) {
        if (CollectionUtils.isEmpty(tenantCodeList)) {
            return Collections.emptyList();
        }
        return this.sysTenantService.lambdaQuery()
                .in(SysTenantPO::getTenantCode, tenantCodeList)
                .eq(SysTenantPO::getUseYn, Boolean.TRUE)
                .list().stream()
                .map(item -> {
                    SysTenantDTO dto = new SysTenantDTO();
                    BeanUtils.copyProperties(item, dto);
                    return dto;
                }).toList();
    }
}
