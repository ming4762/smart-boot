package com.smart.framework.auth.core.model;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 登录成功返回信息
 * @author ShiZhongMing
 * 2020/12/30 15:56
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult implements Serializable {

    @Serial
    private static final long serialVersionUID = -5416368660527838625L;

    private RestUserDetails user;

    private String token;

    private String refreshToken;

    private Set<AuthRole> roles;

    private Set<String> permissions;
}
