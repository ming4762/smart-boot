package com.smart.commons.core.exception;

import java.io.IOException;

/**
 * @author ShiZhongMing
 * 2022/2/9
 * @since 1.0
 */
public class IORuntimeException extends RuntimeException{
    private static final long serialVersionUID = 8851468712570828762L;

    public IORuntimeException(IOException e) {
        super(e);
    }
}
