package com.smart.system.pojo.dto.tenant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 保存租户套餐
 * @author shizhongming
 * 2024/4/5 21:05
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysTenantPackageSaveFunctionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5780220805930917493L;

    @NotNull(message = "租户套餐ID不能为空")
    private Long tenantPackageId;

    @NotNull(message = "菜单ID不能为空")
    private List<Long> functionIdList;

    private List<Long> halfFunctionIdList;
}
