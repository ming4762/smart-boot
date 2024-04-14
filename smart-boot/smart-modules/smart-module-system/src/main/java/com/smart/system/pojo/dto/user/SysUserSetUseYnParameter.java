package com.smart.system.pojo.dto.user;

import com.smart.crud.parameter.SetUseYnParameter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author shizhongming
 * 2024/4/14 16:19
 * @since 3.0.0
 */
@Getter
@Setter
public class SysUserSetUseYnParameter extends SetUseYnParameter {

    @Serial
    private static final long serialVersionUID = 7630786866990816833L;
    /**
     * 是否更新所有租户
     */
    private List<Long> tenantIdList;
}
