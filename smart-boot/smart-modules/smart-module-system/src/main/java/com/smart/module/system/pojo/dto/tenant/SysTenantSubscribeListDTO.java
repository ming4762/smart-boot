package com.smart.module.system.pojo.dto.tenant;

import com.smart.framework.crud.query.PageSortQuery;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 租户订阅查询接口
 * @author shizhongming
 * 2024/4/8 15:40
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SysTenantSubscribeListDTO extends PageSortQuery {
    @Serial
    private static final long serialVersionUID = 2444339596792453514L;

    @NotNull(message = "租户ID不能为空")
    private Long tenantId;
}
