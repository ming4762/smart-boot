package com.smart.module.sso.server.mananger.pojo.parameter;

import com.smart.module.sso.server.common.manager.constants.SsoOauth2UserAccessStrategyEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 客户端绑定用户参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-21 17:17
 * @since 5.0.0
 */
@Getter
@Setter
public class SsoClientBindUserParameter implements Serializable {

    @NotNull(message = "客户端ID不能为空")
    private Long clientId;

    @NotEmpty(message = "用户ID列表不能为空")
    private List<Long> userIdList;

    @NotNull(message = "访问策略不能为空")
    private SsoOauth2UserAccessStrategyEnum accessStrategy;

    @NotNull(message = "启用状态不能为空")
    private Boolean useYn;
}
