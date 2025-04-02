package com.smart.module.system.pojo.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色设置用户DTO
 * @author shizhongming
 * 2025/4/2 15:44
 * @since 5.0.0
 */
@Getter
@Setter
public class RoleSetUserWithTenantDTO extends RoleSetUserDTO {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
