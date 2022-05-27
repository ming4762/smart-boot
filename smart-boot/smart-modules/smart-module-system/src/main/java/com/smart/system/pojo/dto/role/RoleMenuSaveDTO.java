package com.smart.system.pojo.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2020/9/29 10:31 下午
 */
@Getter
@Setter
@ToString
public class RoleMenuSaveDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4698594343302930017L;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotNull(message = "菜单ID不能为空")
    private List<Long> functionIdList;
}
