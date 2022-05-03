package com.smart.auth.core.service;

import com.smart.auth.core.model.AuthUser;
import com.smart.auth.core.model.Permission;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

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
    AuthUser getByUsername(@NonNull String username);

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
     * 查询角色列表
     * @param authUser 用户信息
     * @return 角色列表
     */
    Set<String> listRoleCode(@NonNull AuthUser authUser);

    /**
     * 查询权限列表
     * @param authUser 用户信息
     * @return 权限列表
     */
    Set<Permission> listPermission(@NonNull AuthUser authUser);


}
