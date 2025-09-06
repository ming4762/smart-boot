package com.smart.module.system.pojo.vo;

import com.smart.module.api.system.dto.SysTenantDTO;
import com.smart.module.system.inject.SysTenantInject;
import com.smart.module.system.model.auth.SysAuthAccessSecretPO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * AccessSecret VO
 * @author shizhongming
 * 2024/5/7 20:25
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SysAuthAccessSecretListVO extends SysAuthAccessSecretPO implements SysTenantInject {
    @Serial
    private static final long serialVersionUID = -2998353517922789665L;

    private SysTenantDTO tenant;

}
