package com.smart.commons.core.exception;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/6/30 10:31
 * @since 1.0
 */
public class SystemException extends BaseException{
    @Serial
    private static final long serialVersionUID = 3456037296888119325L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Integer code, String message, Throwable e) {
        super(code, message, e);
    }

    public SystemException(String message, Throwable e) {
        super(message, e);
    }

    public SystemException(Throwable e) {
        super(e);
    }
}
