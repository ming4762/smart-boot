package com.smart.module.api.system.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录查询结果
 * @author zhongming4762
 * 2023/6/7
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 546286084199143374L;
    /**
     * 用户信息
     */
    private Long userId;

    private String username;

    private String fullName;

    private String mobile;

    private String password;

}
