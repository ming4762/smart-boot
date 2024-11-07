package com.smart.module.system.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.auth.core.secret.AccessSecretProvider;
import com.smart.framework.auth.core.secret.data.AccessSecretData;
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import com.smart.module.system.model.tenant.SysTenantPO;
import com.smart.module.system.service.auth.SysAuthAccessSecretService;
import com.smart.module.system.service.tenant.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2023/10/27 17:07
 * @since 3.0.0
 */
@Component
@RequiredArgsConstructor
public class DbAccessSecretProvider implements AccessSecretProvider {

    private final SysAuthAccessSecretService sysAuthAccessSecretService;
    private final SysTenantService sysTenantService;


    /**
     * 通过 accessKey 获取认证信息
     *
     * @param accessKey accessKey
     * @return 认证信息
     */
    @Override
    public AccessSecretData get(String accessKey) {
        SysAuthAccessSecretPO sysAuthAccessSecret = this.sysAuthAccessSecretService.getOne(
                new QueryWrapper<SysAuthAccessSecretPO>().lambda()
                        .eq(SysAuthAccessSecretPO::getAccessKey, accessKey)
                        .eq(SysAuthAccessSecretPO::getUseYn, Boolean.TRUE)
        );
        if (sysAuthAccessSecret == null) {
            return null;
        }
        // 查询租户信息
        SysTenantPO sysTenant = this.sysTenantService.getById(sysAuthAccessSecret.getTenantId());
        UserTenantDTO userTenant = UserTenantDTO.builder()
                .tenantId(sysTenant.getId())
                .tenantCode(sysTenant.getTenantCode())
                .tenantName(sysTenant.getTenantName())
                .tenantShortName(sysTenant.getTenantShortName())
                .platformYn(sysTenant.getPlatformYn())
                .useYn(sysTenant.getUseYn())
                .build();
        return new AccessSecretData(sysAuthAccessSecret.getAccessKey(), sysAuthAccessSecret.getSecretKey(), sysAuthAccessSecret.getExpireDate(), sysAuthAccessSecret.getAccessIp(), userTenant);
    }
}
