package com.smart.framework.commons.core.dto.auth;

import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
