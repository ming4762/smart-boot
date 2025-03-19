package com.smart.framework.commons.core.dto.auth;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色信息
 * @author shizhongming
 * 2024/4/10 15:30
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 3471398088236191399L;

    private Long roleId;

    private String roleCode;

    private String roleName;

    private Boolean superAdminYn;
}
