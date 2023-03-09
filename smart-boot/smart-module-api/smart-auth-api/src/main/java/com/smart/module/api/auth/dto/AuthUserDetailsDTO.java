package com.smart.module.api.auth.dto;

import com.smart.commons.core.dto.auth.UserRolePermission;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/3/9 20:26
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDetailsDTO implements Serializable {

    private Long userId;

    private String username;

    private String fullName;

    private String locale;

    private LocalDateTime loginTime;

    private UserRolePermission rolePermission;

    private String loginIp;

    private Boolean bindIp;

    private List<String> ipWhiteList;

}
