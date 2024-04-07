package com.smart.system.pojo.dto.tenant;

import com.smart.crud.query.PageSortQuery;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 查询未绑定租户的用户参数
 * @author shizhongming
 * 2024/4/7 14:29
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SysTenantListNoBindUserDTO extends PageSortQuery {
    @Serial
    private static final long serialVersionUID = -6101293357939388038L;

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
