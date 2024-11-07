package com.smart.module.api.system.dto;

import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户账户锁定DTO
 * @author zhongming4762
 * 2023/3/11
 */
@Getter
@Setter
@ToString
public class UserAccountLockDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -5073969003934135717L;
    private String username;

    private UserAccountStatusEnum accountStatus;

    /**
     * 租户ID
     */
    @NonNull
    private Long tenantId;
}
