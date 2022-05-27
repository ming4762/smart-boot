package com.smart.system.pojo.dto.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/6/17 16:55
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class RoleSetUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 9127937213126225406L;
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    private List<Long> userIdList;
}
