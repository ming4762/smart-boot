package com.smart.auth.extensions.appsecret.exception;

import com.smart.auth.core.exception.AuthException;
import com.smart.commons.core.http.IHttpStatus;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class AuthAppsecretException extends AuthException {
    private static final long serialVersionUID = 7591042120913152080L;

    public AuthAppsecretException(IHttpStatus status) {
        super(status);
    }

    public AuthAppsecretException(Integer code, String message) {
        super(code, message);
    }

    public AuthAppsecretException(String message) {
        super(message);
    }

    public AuthAppsecretException(String message, Throwable e) {
        super(message, e);
    }
}
