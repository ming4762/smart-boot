package com.smart.smc.inter.sdport.pay.exception;

import com.business.inter.sdport.pay.pojo.result.SdportCommonResult;

import java.io.Serial;

/**
 * 山港云付用户未注册异常
 * @author shizhongming
 * 2024/11/9 16:39
 * @since 1.0.0
 */
public class SdportPayUnRegisterException extends SdportPayException {
    @Serial
    private static final long serialVersionUID = 2878593587614070084L;

    public SdportPayUnRegisterException(String message, SdportCommonResult result) {
        super(message, result);
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SdportPayUnRegisterException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public SdportPayUnRegisterException() {
        super();
    }
}
