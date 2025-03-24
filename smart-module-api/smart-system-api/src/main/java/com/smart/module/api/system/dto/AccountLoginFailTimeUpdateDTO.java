package com.smart.module.api.system.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/3/11
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginFailTimeUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8150432747535120496L;

    private String username;

    private Long loginFailTime;

    private Long tenantId;
}
