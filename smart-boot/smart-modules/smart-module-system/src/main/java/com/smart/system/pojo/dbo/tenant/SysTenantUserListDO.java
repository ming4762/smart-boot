package com.smart.system.pojo.dbo.tenant;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询租户对用用户
 * @author shizhongming
 * 2024/4/6 20:30
 * @since 3.0.0
 */
@Getter
@Setter
public class SysTenantUserListDO implements Serializable {


    @Serial
    private static final long serialVersionUID = -5499458337788751293L;

    private Long id;

    private Long tenantId;

    private Long userId;

    private String username;

    private String fullName;

    private String mobile;

    private String email;

    /**
     * create_time - createTime
     */
    private LocalDateTime createTime;

    /**
     * create_user_id - createUserId
     */
    private Long createUserId;

    /**
     * create_by - createBy
     */
    private String createBy;
}
