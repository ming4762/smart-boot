package com.smart.system.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.secret.AccessSecretProvider;
import com.smart.auth.core.secret.data.AccessSecretData;
import com.smart.system.model.auth.SysAuthAccessSecretPO;
import com.smart.system.service.auth.SysAuthAccessSecretService;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2023/10/27 17:07
 * @since 3.0.0
 */
@Component
public class DbAccessSecretProvider implements AccessSecretProvider {

    private final SysAuthAccessSecretService sysAuthAccessSecretService;

    public DbAccessSecretProvider(SysAuthAccessSecretService sysAuthAccessSecretService) {
        this.sysAuthAccessSecretService = sysAuthAccessSecretService;
    }

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
        return new AccessSecretData(sysAuthAccessSecret.getAccessKey(), sysAuthAccessSecret.getSecretKey(), sysAuthAccessSecret.getExpireDate(), sysAuthAccessSecret.getAccessIp());
    }
}
