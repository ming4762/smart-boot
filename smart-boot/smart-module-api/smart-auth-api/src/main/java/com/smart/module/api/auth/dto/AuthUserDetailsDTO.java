package com.smart.module.api.auth.dto;

import com.smart.framework.commons.core.dto.auth.UserAccountData;
import lombok.*;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = -532782647478139020L;
    private Long userId;

    private String username;

    private String fullName;

    private String locale;

    private LocalDateTime loginTime;

    private UserAccountData userAccountData;

    private String loginIp;

    private Boolean bindIp;

    private List<String> ipWhiteList;

}
