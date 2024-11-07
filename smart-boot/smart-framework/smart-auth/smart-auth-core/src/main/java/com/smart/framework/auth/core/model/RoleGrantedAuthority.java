package com.smart.framework.auth.core.model;

import com.smart.framework.auth.core.constants.GrantedAuthorityTypeEnum;
import com.smart.framework.commons.core.dto.auth.AuthRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serial;

/**
 * @author jackson
 * 2020/1/29 9:04 下午
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleGrantedAuthority implements SmartGrantedAuthority {
    @Serial
    private static final long serialVersionUID = 4316900997007482876L;

    private static final String ROLE_START = "ROLE_";

    private AuthRole authRole;

    @Override
    @NonNull
    public String getAuthority() {
        return ROLE_START + this.authRole.getRoleCode();
    }

    @Override
    public boolean isRole() {
        return true;
    }

    @Override
    public boolean isPermission() {
        return false;
    }

    @Override
    public GrantedAuthorityTypeEnum getType() {
        return GrantedAuthorityTypeEnum.ROLE;
    }

    @Override
    public String getAuthorityValue() {
        return this.authRole.getRoleCode();
    }
}
