package com.smart.framework.auth.core.event;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * IP绑定认证错误事件
 * @author ShiZhongMing
 * 2021/12/29
 * @since 1.0.7
 */
public class AuthenticationFailureIpBindEvent extends AbstractAuthenticationFailureEvent {
    @Serial
    private static final long serialVersionUID = -6390551448185064894L;

    public AuthenticationFailureIpBindEvent(Authentication authentication, AuthenticationException exception) {
        super(authentication, exception);
    }
}
