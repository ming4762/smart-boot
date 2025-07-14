package com.smart.module.system.pojo.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 通过角色ID&租户ID查询用户信息
 * @author shizhongming
 * 2025/4/2 16:34
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class ListUserByRoleTenantDTO implements Serializable {

    private Long tenantId;

    @NotNull(message = "角色ID不能为空")
    private List<Long> roleIdList;
}
