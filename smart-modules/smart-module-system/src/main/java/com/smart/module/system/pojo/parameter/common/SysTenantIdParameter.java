package com.smart.module.system.pojo.parameter.common;

import com.smart.framework.crud.query.IdParameter;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统租户ID参数
 * @author shizhongming
 * 2025/4/16 14:44
 * @since 5.0.0
 */
@Getter
@Setter
public class SysTenantIdParameter extends IdParameter {

    private Long tenantId;
}
