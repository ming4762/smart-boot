package com.smart.module.system.pojo.dbo.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 查询用户对应租户DO
 * @author shizhongming
 * 2024/4/8 18:18
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysTenantListByUserDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5424902558705517332L;

    private Long tenantId;

    private String tenantCode;

    private String tenantName;

    private String tenantShortName;

    /**
     * 是否平台管理租户
     */
    private Boolean platformYn;

    private Boolean useYn;
}
