package com.smart.module.api.system.dto;

import com.smart.module.api.system.constants.UserAccountStatusEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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


    private String username;

    private UserAccountStatusEnum accountStatus;
}
