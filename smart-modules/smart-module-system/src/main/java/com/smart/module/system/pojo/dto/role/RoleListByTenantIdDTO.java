package com.smart.module.system.pojo.dto.role;

import com.smart.framework.crud.query.PageSortQuery;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shizhongming
 * 2025/4/2 10:34
 * @since 5.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RoleListByTenantIdDTO extends PageSortQuery {

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
