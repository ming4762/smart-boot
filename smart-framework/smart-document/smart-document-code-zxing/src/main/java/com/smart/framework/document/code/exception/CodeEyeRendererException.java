package com.smart.framework.document.code.exception;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/8/11 16:09
 * @since 1.0
 */
public class CodeEyeRendererException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1525440481275794650L;

    public CodeEyeRendererException() {
        super();
    }

    public CodeEyeRendererException(String message) {
        super(message);
    }
}
