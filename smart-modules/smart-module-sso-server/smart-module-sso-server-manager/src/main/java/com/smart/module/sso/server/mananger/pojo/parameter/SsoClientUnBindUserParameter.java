package com.smart.module.sso.server.mananger.pojo.parameter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 客户端解绑用户参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-21 21:53
 * @since 5.0.0
 */
@Getter
@Setter
public class SsoClientUnBindUserParameter implements Serializable {

    @NotNull(message = "客户端ID不能为空")
    private Long clientId;

    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIdList;
}
