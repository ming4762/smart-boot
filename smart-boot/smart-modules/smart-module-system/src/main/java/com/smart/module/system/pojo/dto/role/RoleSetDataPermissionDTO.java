package com.smart.module.system.pojo.dto.role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 角色数据权限关系表
 * @author shizhongming
 * 2025/3/11 11:17
 * @since 5.0.0
 */
@Getter
@Setter
public class RoleSetDataPermissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6627800630029723609L;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotEmpty(message = "数据权限ID不能为空")
    private List<Long> dataPermissionIdList;
}
