package com.smart.module.sso.server.auth.exception;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;

/**
 * 客户端用户未授权异常
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-24 10:38
 * @since 5.0.0
 */
@Getter
public class OAuth2ClientUserNotAllowException extends OAuth2AuthenticationException {

    private final OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication;

    public OAuth2ClientUserNotAllowException(OAuth2Error error, @Nullable OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication) {
        super(error);
        this.authorizationCodeRequestAuthentication = authorizationCodeRequestAuthentication;
    }

    public OAuth2ClientUserNotAllowException(
            OAuth2Error error,
            Throwable cause,
            @Nullable OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication
    ) {
        super(error, cause);
        this.authorizationCodeRequestAuthentication = authorizationCodeRequestAuthentication;
    }
}
