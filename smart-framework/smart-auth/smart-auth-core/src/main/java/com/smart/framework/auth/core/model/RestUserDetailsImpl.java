package com.smart.framework.auth.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.constants.LoginTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import com.smart.framework.commons.core.dto.auth.Permission;
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/16 9:11 下午
 */
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestUserDetailsImpl implements RestUserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = -6184955894751051086L;

    @Getter
    private Long userId;

    private String username;

    private String password;

    @Getter
    private String fullName;

    private String token;

    private String locale;

    private Boolean enabled;

    @Builder.Default
    private Set<PermissionGrantedAuthority> permissions = HashSet.newHashSet(0);

    @Builder.Default
    private Set<RoleGrantedAuthority> roles = HashSet.newHashSet(0);

    @Getter
    private ZonedDateTime loginTime;

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

    @Getter
    private UserTenantDTO userTenant;

    /**
     * 账户是否锁定
     */
    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Boolean accountNonLocked;

    @Override
    @JsonIgnore
    public Collection<SmartGrantedAuthority> getAuthorities() {
        Set<SmartGrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(this.permissions);
        authorities.addAll(this.roles);
        return authorities;
    }

    /**
     * 获取用户的角色编码
     * @return 用户角色列表
     */
    @Override
    @NonNull
    public Set<AuthRole> getRoles() {
        if (Objects.isNull(this.roles)) {
            return Set.of();
        }
        return this.roles.stream()
                .map(RoleGrantedAuthority::getAuthRole)
                .collect(Collectors.toSet());
    }

    /**
     * 获取用户的权限列表
     * @return 用户权限列表
     */
    @Override
    @NonNull
    public Set<Permission> getPermissions() {
        if (Objects.isNull(this.permissions)) {
            return Set.of();
        }
        return this.permissions.stream()
                .map(PermissionGrantedAuthority::getPermission)
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
        return !Boolean.FALSE.equals(this.enabled);
    }

}
