package com.smart.auth.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.constants.LoginTypeEnum;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.module.api.system.dto.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/16 9:11 下午
 */
@Setter
@NoArgsConstructor
public class RestUserDetailsImpl implements RestUserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = -6184955894751051086L;

    @Getter
    private Long userId;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter
    private String fullName;

    private String token;

    private String locale;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<SmartGrantedAuthority> authorities;

    @Getter
    private LocalDateTime loginTime;

    /**
     * 标识登录类型
     */
    @Getter
    private LoginTypeEnum loginType;

    /**
     * 认证类型
     */
    @Getter
    private AuthTypeEnum authType;

    @Getter
    private List<String> ipWhiteList;

    @Getter
    private Boolean bindIp;

    @Getter
    private String loginIp;

    @Getter
    private Long loginFailTime;

    /**
     * 账户是否锁定
     */
    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Boolean accountNonLocked;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Collection<SmartGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 获取用户的角色编码
     * @return 用户角色列表
     */
    @Override
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Set<String> getRoles() {
        if (Objects.isNull(this.authorities)) {
            return Sets.newHashSet();
        }
        return this.authorities.stream()
                .filter(SmartGrantedAuthority::isRole)
                .map(SmartGrantedAuthority::getAuthorityValue)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的权限列表
     * @return 用户权限列表
     */
    @Override
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Set<Permission> getPermissions() {
        if (Objects.isNull(this.authorities)) {
            return Sets.newHashSet();
        }
        return this.authorities.stream()
                .filter(SmartGrantedAuthority :: isPermission)
                .map(item -> ((PermissionGrantedAuthority)item).getPermission())
                .collect(Collectors.toSet());
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getToken() {
        return this.token;
    }

    @Override
    public String getLocale() {
        return this.locale;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(this.accountNonLocked);
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }

}
