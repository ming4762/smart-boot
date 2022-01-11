package com.smart.auth.core.exception;

import com.smart.commons.core.http.IHttpStatus;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * @author jackson
 * 2020/2/15 12:53 下午
 */
@Getter
public class AuthException extends AuthenticationException {
    private static final long serialVersionUID = -6922142660105351058L;

    private final Integer code;

    public AuthException(IHttpStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public AuthException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthException(String message) {
        super(message);
        this.code = 401;
    }

    public AuthException(String message, Throwable e) {
        super(message, e);
        this.code = 401;
    }
}
