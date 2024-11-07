package com.smart.module.system.pojo.dto.tenant;

import com.smart.framework.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 查询租户列表参数
 * @author shizhongming
 * 2024/4/6 20:36
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SysTenantUserListDTO extends PageSortQuery {
    @Serial
    private static final long serialVersionUID = -3477956748273299223L;

    private Long tenantId;

    private String fullName;

    private String username;
}
