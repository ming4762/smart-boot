package com.smart.commons.core.exception;

import com.smart.commons.core.http.HttpStatus;
import lombok.Getter;

/**
 * @author jackson
 * 2020/1/28 3:15 下午
 */
@Getter
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5741320275497601133L;

    private final Integer code;

    private final String message;

    private final Throwable e;

    public BaseException(String message) {
        this(message, null);
    }

    public BaseException(Integer code, String message, Throwable e) {
        super(message, e);
        this.code = code;
        this.message = message;
        this.e = e;
    }

    public BaseException(String message, Throwable e) {
        this(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), message, e);
    }

    public BaseException(Throwable e) {
        this(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), e);
    }
}
