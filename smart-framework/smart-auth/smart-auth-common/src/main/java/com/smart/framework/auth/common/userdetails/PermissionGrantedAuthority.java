package com.smart.framework.auth.common.userdetails;

import com.smart.framework.commons.core.dto.auth.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @author jackson
 * 2020/1/29 9:06 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionGrantedAuthority implements SmartGrantedAuthority {

    @Serial
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
