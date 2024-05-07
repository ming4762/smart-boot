package com.smart.system.pojo;

import com.smart.system.inject.SysTenantInject;
import com.smart.system.model.auth.SysAuthAccessSecretPO;
import com.smart.system.model.tenant.SysTenantPO;
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

    private SysTenantPO tenant;

}
