package com.smart.module.api.system;

import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.dto.AuthUser;
import lombok.NonNull;
import org.springframework.lang.Nullable;

/**
 * 获取认证用户的API
 * @author zhongming4762
 * 2023/3/8
 */
public interface SystemAuthUserApi {

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
