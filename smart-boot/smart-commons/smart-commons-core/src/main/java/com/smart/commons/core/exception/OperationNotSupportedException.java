package com.smart.commons.core.exception;

import java.io.Serial;

/**
 * 不支持操作异常
 * @author shizhongming
 * 2020/9/1 9:39 下午
 */
public class OperationNotSupportedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8155827583565775895L;

    public OperationNotSupportedException(Throwable throwable) {
        super(throwable);
    }

    public OperationNotSupportedException(String message) {
        super(message);
    }

    public OperationNotSupportedException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
