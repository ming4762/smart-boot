package com.smart.module.system.pojo.dto.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/4/10 17:27
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysListTenantFunctionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7739408761596992755L;

    private ZonedDateTime now;

    private Long tenantId;

    public SysListTenantFunctionDTO() {
        this.now = ZonedDateTime.now();
    }
}
