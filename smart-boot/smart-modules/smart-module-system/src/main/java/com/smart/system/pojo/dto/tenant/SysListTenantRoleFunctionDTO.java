package com.smart.system.pojo.dto.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 *
 * @author shizhongming
 * 2024/4/10 14:21
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SysListTenantRoleFunctionDTO extends SysListTenantFunctionDTO {
    @Serial
    private static final long serialVersionUID = -3411808224915159288L;

    private List<Long> roleIdList;
}
