package com.smart.auth.core.service;

import com.smart.auth.core.model.AuthUser;
import com.smart.auth.core.model.UserRolePermission;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author jackson
 * 2020/1/23 7:39 下午
 */
public interface AuthUserService {

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Nullable
    default AuthUser getByUsername(@NonNull String username) {
        return null;
    }

    /**
     * 通过手机号查询用户
     * @param phone 手机号
     * @return 用户信息
     */
    @Nullable
    default AuthUser getByPhone(@NonNull String phone) {
        return null;
    }

    /**
     * 查询用户角色权限信息
     * @param authUser 用户信息
     * @return 权限角色信息
     */
    UserRolePermission queryRolePermission(@NonNull AuthUser authUser);
}
