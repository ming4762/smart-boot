package com.smart.auth.extensions.saml2.userdetails;

import com.google.common.collect.Sets;
import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.model.RoleGrantedAuthority;
import com.smart.auth.core.model.PermissionGrantedAuthority;
import com.smart.auth.core.model.SmartGrantedAuthority;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUser;
import com.smart.module.api.system.dto.UserRolePermission;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/1/7 17:14
 * @since 1.0
 */
public class DefaultSamlUserDetailsServiceImpl implements SAMLUserDetailsService {

    private final SystemAuthUserApi systemAuthUserApi;

    public DefaultSamlUserDetailsServiceImpl(SystemAuthUserApi systemAuthUserApi) {
        this.systemAuthUserApi = systemAuthUserApi;
    }

    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        // 获取用户名
        String username = credential.getNameID().getValue();
        // 查询用户
        final AuthUser user = this.systemAuthUserApi.getByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException(String.format("not found user, please create user in DB, username: [%s]", username));
        }
        Set<SmartGrantedAuthority> grantedAuthoritySet = Sets.newHashSet();
        // 添加角色
        UserRolePermission userRolePermission = this.systemAuthUserApi.queryRolePermission(user);
        grantedAuthoritySet.addAll(

                userRolePermission.getRoleCodes().stream()
                        .map(RoleGrantedAuthority::new).collect(Collectors.toList())
        );
        // 添加权限
        grantedAuthoritySet.addAll(
                userRolePermission.getPermissions()
                        .stream()
                        .map(PermissionGrantedAuthority::new).collect(Collectors.toList())
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
        restUserDetails.setAuthType(AuthTypeEnum.SAML2);
        return restUserDetails;
    }
}
