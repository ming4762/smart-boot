package com.smart.commons.core.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/4/8 18:50
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class UserTenantDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3827700705548774217L;

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
