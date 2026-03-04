package com.smart.framework.auth.common.exception;

import com.smart.framework.commons.core.http.IHttpStatus;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

/**
 * HttpStatus 认证异常
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-04 13:35
 * @since 5.0.0
 */
@Getter
public class AuthHttpStatusException  extends AuthenticationException {

    private final IHttpStatus httpStatus;

    public AuthHttpStatusException(IHttpStatus httpStatus, Throwable cause) {
        super(httpStatus.getMessage(), cause);
        this.httpStatus = httpStatus;
    }

    public AuthHttpStatusException(IHttpStatus httpStatus) {
        this(httpStatus, null);
    }
}
