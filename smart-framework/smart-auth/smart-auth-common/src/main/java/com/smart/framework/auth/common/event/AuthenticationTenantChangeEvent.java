package com.smart.framework.auth.common.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/4/12 14:55
 * @since 3.0.0
 */
@Getter
@Setter
public class AuthenticationTenantChangeEvent extends AbstractAuthenticationEvent {

    @Serial
    private static final long serialVersionUID = -8536618597852163992L;

    private Authentication oldAuthentication;

    public AuthenticationTenantChangeEvent() {
        super(null);
    }

    public AuthenticationTenantChangeEvent(Authentication oldAuth, Authentication newAuth) {
        super(newAuth);
        this.oldAuthentication = oldAuth;
    }
}
