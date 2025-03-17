package com.smart.module.api.crud.module;

import lombok.*;

/**
 * 数据上下文用户信息
 * @author shizhongming
 * 2025/3/5 20:01
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartDataContextUserModel {

    private Long userId;

    private String username;

    private String fullName;

    private Long tenantId;

    private String tenantCode;

    private Boolean isSuperAdmin;
}
