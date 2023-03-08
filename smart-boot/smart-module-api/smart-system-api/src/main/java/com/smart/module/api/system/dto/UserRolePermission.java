package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户的角色权限信息
 * @author ShiZhongMing
 * 2022/8/23
 * @since 3.0.0
 */
@Getter
@Setter
public class UserRolePermission implements Serializable {

    private static final long serialVersionUID = -3632192598130979878L;

    public UserRolePermission() {
        this.roleCodes = new HashSet<>(0);
        this.permissions = new HashSet<>(0);
    }

    /**
     * 角色编码
     */
    private Set<String> roleCodes;

    /**
     * 权限信息
     */
    private Set<Permission> permissions;
}
