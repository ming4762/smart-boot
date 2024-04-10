package com.smart.auth.core.utils;

import com.smart.auth.core.exception.AuthException;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.dto.auth.UserTenantDTO;
import com.smart.commons.core.exception.SystemException;
import com.smart.commons.core.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * 认证工具类
 * @author
 * 2020/1/22 7:07 下午
 */
@Slf4j
public final class AuthUtils {

    private static final String NOT_LOGIN_USER = "anonymousUser";

    private AuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 是否是超级管理员
     * @return 是否是超级管理员
     */
    public static boolean isSuperAdmin() {
        return Optional.ofNullable(getCurrentUser())
                .map(RestUserDetails:: getRoles)
                .orElse(new HashSet<>())
                .stream().anyMatch(item -> Boolean.TRUE.equals(item.getSuperAdminYn()));
    }

    /**
     * 获取当前用户
     * @return 当前用户
     */
    @Nullable
    public static RestUserDetails getCurrentUser() {
        Object principal = Optional.of(SecurityContextHolder.getContext())
                .map(SecurityContext :: getAuthentication)
                .map(Authentication :: getPrincipal)
                .orElse(null);
        if (Objects.isNull(principal) || StringUtils.equals(principal.toString(), NOT_LOGIN_USER)) {
            return null;
        }
        return (RestUserDetails)principal;
    }

    /**
     * 获取当前登录用户（不能为null）
     * @return 当前登录用户
     */
    @NonNull
    public static RestUserDetails getNonNullCurrentUser() {
        RestUserDetails userDetails = getCurrentUser();
        if (Objects.isNull(userDetails)) {
            throw new AuthException(HttpStatus.UNAUTHORIZED);
        }
        return userDetails;
    }

    /**
     * 获取当前登录人员用户名
     * @return 用户名
     */
    public static String getCurrentUsername() {
        return Optional.ofNullable(getCurrentUser())
                .map(UserDetails::getUsername)
                .orElse(null);
    }

    /**
     * 获取当前登录用户ID（不能为null）
     * @return 当前登录用户ID
     */
    @NonNull
    public static Long getNonNullCurrentUserId() {
        return getNonNullCurrentUser().getUserId();
    }

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    @Nullable
    public static Long getCurrentUserId() {
        RestUserDetails user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getUserId();
    }

    /**
     * 判断是否拥有权限
     * @param permission 权限
     * @return true：拥有权限
     */
    public static boolean hasPermission(@NonNull String permission) {
        return getNonNullCurrentUser().getPermissions().stream()
                .anyMatch(item -> StringUtils.equals(permission, item.getAuthority()));
    }

    /**
     * 当前用户是否是平台管理租户
     * @return boolean
     */
    public static boolean isPlatformTenant() {
        return Optional.ofNullable(getCurrentUser())
                .map(RestUserDetails::getUserTenant)
                .map(item -> Boolean.TRUE.equals(item.getPlatformYn()))
                .orElse(false);

    }

    /**
     * 获取当前租户ID
     * @return 租户ID
     */
    public static Long getCurrentTenantId() {
        return Optional.ofNullable(getCurrentUser())
                .map(RestUserDetails::getUserTenant)
                .map(UserTenantDTO::getTenantId)
                .orElse(null);
    }

    /**
     * 获取当前租户ID
     * @return 租户ID
     */
    public static Long getNonNullCurrentTenantId() {
        Long tenantId = Optional.ofNullable(getCurrentUser())
                .map(RestUserDetails::getUserTenant)
                .map(UserTenantDTO::getTenantId)
                .orElse(null);
        if (tenantId == null) {
            throw new SystemException("获取租户失败");
        }
        return tenantId;
    }
}
