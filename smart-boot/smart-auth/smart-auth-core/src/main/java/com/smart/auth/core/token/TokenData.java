package com.smart.auth.core.token;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.dto.auth.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * token信息
 * @author ShiZhongMing
 * 2022/4/12
 * @since 2.0.0
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class TokenData implements Serializable {

    @Serial
    private static final long serialVersionUID = -1673739084242479566L;

    private String token;

    private LocalDateTime createTime;

    private LocalDateTime refreshTime;

    private Duration timeout;

    private RestUserDetails user;

    public TokenData(String token, LocalDateTime createTime, LocalDateTime refreshTime, Duration timeout, RestUserDetails user) {
        this.token = token;
        this.createTime = createTime;
        this.refreshTime = refreshTime;
        this.timeout = timeout;
        this.user = user;
    }

    /**
     * 权限信息
     */
    private Set<Permission> permissions;

    /**
     * 角色信息
     */
    private Set<String> roles;
}
