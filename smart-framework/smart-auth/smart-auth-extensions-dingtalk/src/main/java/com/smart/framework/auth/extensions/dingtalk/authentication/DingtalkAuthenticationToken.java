package com.smart.framework.auth.extensions.dingtalk.authentication;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 钉钉登录token
 * @author shizhongming
 * 2025/10/31 14:20
 * @since 5.0.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DingtalkAuthenticationToken extends AbstractEnhanceAuthenticationToken {

    /**
     * 用户登录时所选组织ID
     */
    private final String corpId;

    public DingtalkAuthenticationToken(String corpId, String credentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(AuthTypeEnum.DINGTALK, credentials, principal, authorities);
        this.corpId = corpId;
    }

    public DingtalkAuthenticationToken(String credentials) {
        this(null, credentials, null, null);
    }
}
