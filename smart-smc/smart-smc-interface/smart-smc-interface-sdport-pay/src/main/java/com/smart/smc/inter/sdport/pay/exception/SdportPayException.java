package com.smart.smc.inter.sdport.pay.exception;

import com.smart.smc.inter.sdport.pay.pojo.result.SdportPayCommonResult;
import lombok.Getter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/10/29 15:15
 * @since 1.0.0
 */
@Getter
public class SdportPayException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8168567600372352708L;

    private SdportPayCommonResult result;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public SdportPayException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SdportPayException(String message) {
        super(message);
    }


    public SdportPayException(String message, SdportPayCommonResult result) {
        super(message);
        this.result = result;
    }
}
