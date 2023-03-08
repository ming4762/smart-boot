package com.smart.auth.core.model;

import com.smart.auth.core.constants.GrantedAuthorityTypeEnum;
import com.smart.module.api.system.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jackson
 * 2020/1/29 9:06 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionGrantedAuthority implements SmartGrantedAuthority {

    private static final long serialVersionUID = -8344886266135685662L;

    private Permission permission;

    @Override
    public boolean isRole() {
        return false;
    }

    @Override
    public boolean isPermission() {
        return true;
    }

    @Override
    public GrantedAuthorityTypeEnum getType() {
        return GrantedAuthorityTypeEnum.PERMISSION;
    }

    @Override
    public String getAuthorityValue() {
        return this.permission.getAuthority();
    }

    @Override
    public String getAuthority() {
        return this.permission.getAuthority();
    }
}
