package com.smart.module.system.pojo.dto.tenant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 租户绑定用户参数
 * @author shizhongming
 * 2024/4/7 16:32
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysTenantRemoveBindUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6520719792136055645L;

    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotEmpty(message = "用户ID不能为空")
    private List<Long> userIdList;
}
