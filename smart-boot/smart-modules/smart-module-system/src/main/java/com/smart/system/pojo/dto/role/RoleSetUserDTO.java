package com.smart.system.pojo.dto.role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/6/17 16:55
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class RoleSetUserDTO {

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    private List<Long> userIdList;
}
