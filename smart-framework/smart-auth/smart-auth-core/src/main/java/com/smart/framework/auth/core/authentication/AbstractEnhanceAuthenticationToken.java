package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/6 10:21
 */
@EqualsAndHashCode(callSuper = true)
public class AbstractEnhanceAuthenticationToken extends AbstractAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 5651300794057062163L;

    private final transient Object credentials;

    private final transient Object principal;

    @Getter
    @Setter
    private String loginIp;

    /**
     * 登录方式
     */
    @Getter
    private final AuthTypeEnum authType;

    public AbstractEnhanceAuthenticationToken(AuthTypeEnum authType, Object credentials, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.authType = authType;
        this.credentials = credentials;
        this.principal = principal;
    }

    public AbstractEnhanceAuthenticationToken(AuthTypeEnum authType, Object credentials, Object principal) {
        super(List.of());
        this.authType = authType;
        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
