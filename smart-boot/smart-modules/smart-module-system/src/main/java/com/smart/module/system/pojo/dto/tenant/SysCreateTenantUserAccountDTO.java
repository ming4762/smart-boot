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
 * 创建租户账户权限
 * @author shizhongming
 * 2024/4/10 10:14
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysCreateTenantUserAccountDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6376516299903536831L;
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @NotEmpty(message = "用户ID不能为空")
    private List<Long> userIdList;
}
