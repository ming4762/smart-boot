package com.smart.auth.core.model;

import com.smart.auth.core.constants.GrantedAuthorityTypeEnum;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author jackson
 * 2020/1/29 9:07 下午
 */
public interface SmartGrantedAuthority extends GrantedAuthority {

    /**
     * 是否是角色
     * @return 是否是角色
     */
    boolean isRole();

    /**
     * 是否是权限
     * @return 是否是权限
     */
    boolean isPermission();

    /**
     * 获取权限类型
     * @return 权限类型
     */
    GrantedAuthorityTypeEnum getType();

    /**
     * 获取权限的值
     * @return 权限的值
     */
    String getAuthorityValue();
}
