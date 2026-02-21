package com.smart.module.sso.server.mananger.pojo.parameter;

import com.smart.framework.crud.query.PageSortQuery;
import com.smart.module.sso.server.common.manager.constants.SsoOauth2UserAccessStrategyEnum;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 查询客户端用户参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-20 23:58
 * @since 5.0.0
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SsoListClientUserParameter extends PageSortQuery {

    /**
     * 客户端ID
     */
    @NotNull(message = "客户端ID不能为空")
    private Long clientId;

    private Boolean clientUserUseYn;

    /**
     * 访问策略
     */
    private SsoOauth2UserAccessStrategyEnum accessStrategy;
}
