package com.smart.module.sso.server.mananger.pojo.parameter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 设置客户端用户启用状态参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-21 20:31
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SsoClientUserUseYnParameter implements Serializable {

    @NotNull(message = "客户端ID不能为空")
    private Long clientId;

    @NotNull(message = "启用状态不能为空")
    private Boolean useYn;

    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIdList;
}
