package com.smart.auth.security.userdetails;

import com.google.common.collect.Sets;
import com.smart.auth.core.model.PermissionGrantedAuthority;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.model.RoleGrantedAuthority;
import com.smart.auth.core.model.SmartGrantedAuthority;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.userdetails.SmsUserDetailService;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUser;
import com.smart.module.api.system.dto.UserRolePermission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认的UserDetailsService
 * @author shizhongming
 * 2020/1/23 7:34 下午
 */
public class DefaultUserDetailsServiceImpl implements UserDetailsService, SmsUserDetailService {

    private final SystemAuthUserApi systemAuthUserApi;

    public DefaultUserDetailsServiceImpl(SystemAuthUserApi systemAuthUserApi) {
        this.systemAuthUserApi = systemAuthUserApi;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        if (StringUtils.isEmpty(username)) {
            return null;
        }
        // 查询用户
        final AuthUser user = this.systemAuthUserApi.getByUsername(username);
        return this.getUserDetails(user);
    }

    /**
     * 获取用户信息
     * @param user 用户
     * @return 用户detail
     */
    protected RestUserDetails getUserDetails(@Nullable AuthUser user) {
        if (Objects.isNull(user)) {
            return null;
        }
        Set<SmartGrantedAuthority> grantedAuthoritySet = Sets.newHashSet();
        UserRolePermission userRolePermission = this.systemAuthUserApi.queryRolePermission(user);
        // 添加角色
        grantedAuthoritySet.addAll(
                userRolePermission.getRoleCodes().stream()
                        .map(RoleGrantedAuthority::new).collect(Collectors.toSet())
        );
        // 添加权限
        grantedAuthoritySet.addAll(
                userRolePermission.getPermissions().stream()
                        .map(PermissionGrantedAuthority::new).toList()
        );
        // 查询用户角色信息
        final RestUserDetailsImpl restUserDetails = createByUser(user);
        restUserDetails.setAuthorities(grantedAuthoritySet);
        return restUserDetails;
    }

    @NonNull
    protected static RestUserDetailsImpl createByUser(@NonNull AuthUser user) {
        final RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
        restUserDetails.setUserId(user.getUserId());
        restUserDetails.setFullName(user.getFullName());
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setPassword(user.getPassword());
        restUserDetails.setAccountNonLocked(!(Boolean.TRUE.equals(user.getLocked())));
        restUserDetails.setLoginFailTime(user.getLoginFailTime());
        restUserDetails.setIpWhiteList(user.getIpWhiteList());
        return restUserDetails;
    }

    /**
     * 通过手机号查询用户信息
     * @param phone 手机号
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        // 查询用户
        final AuthUser user = this.systemAuthUserApi.getByPhone(phone);
        return this.getUserDetails(user);
    }
}
