package com.smart.module.api.system.dto;

import lombok.*;

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

    private String username;

    private Long loginFailTime;
}
