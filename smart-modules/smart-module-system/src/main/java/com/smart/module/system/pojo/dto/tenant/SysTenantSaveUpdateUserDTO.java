package com.smart.module.system.pojo.dto.tenant;

import com.smart.module.system.pojo.dto.user.UserSaveUpdateWithDeptDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 租户管理添加修改用户DTO
 * @author shizhongming
 * 2025/4/16 10:59
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SysTenantSaveUpdateUserDTO extends UserSaveUpdateWithDeptDTO {

    /**
     * 是否创建账户
     */
    private Boolean createAccount;

    /**
     * 租户ID,保存时有效,更新时无效
     */
    private Long tenantId;
}
