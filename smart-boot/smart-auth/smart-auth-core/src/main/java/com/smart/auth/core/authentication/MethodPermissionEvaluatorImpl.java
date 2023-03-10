package com.smart.auth.core.authentication;

import com.smart.auth.core.constants.RoleEnum;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.dto.auth.Permission;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 自定义的方法权限认证
 * @author zhongming4762
 * 2020/1/24 10:53 上午
 */
public class MethodPermissionEvaluatorImpl implements PermissionEvaluator {

    private final Boolean developmentMode;

    public MethodPermissionEvaluatorImpl(Boolean developmentMode) {
        this.developmentMode = developmentMode;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 开发模式不拦截
        if (BooleanUtils.isTrue(developmentMode)) {
            return true;
        }
        // 验证角色，超级管理员角色不拦截
        final RestUserDetails user = (RestUserDetails) authentication.getPrincipal();
        if (user.getRoles().contains(RoleEnum.ROLE_SUPERADMIN.getRole())) {
            return true;
        }
        // 验证权限
        final String permissionStr = String.join(":", targetDomainObject.toString(), permission.toString());
        Set<String> permissions = user.getPermissions().stream()
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
        return permissions.contains(permissionStr);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
