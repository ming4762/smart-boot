package com.smart.module.system.pojo.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author shizhongming
 * 2025/4/16 15:12
 * @since 5.0.0
 */
@Getter
@Setter
public class SysUserDeleteDTO implements Serializable {

    @NotEmpty(message = "用户ID不能为空")
    private List<Long> userIdList;

    /**
     * 指定租户删除用户
     * 校验用户是否与其他租户绑定
     */
    private Long tenantId;
}
