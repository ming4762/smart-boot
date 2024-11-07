package com.smart.framework.document.exception;

import com.smart.framework.document.constants.DocumentFormatEnum;

import java.io.Serial;

/**
 * 不支持的转换源格式
 * @author ShiZhongMing
 * 2021/8/27 11:01
 * @since 1.0
 */
public class ConvertToFormatNotSupportException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6581836349539514014L;

    public ConvertToFormatNotSupportException(String message) {
        super(message);
    }

    public ConvertToFormatNotSupportException(DocumentFormatEnum toFormat) {
        super(String.format("to com.smart.framework.tool.code.type is not support: %s", toFormat.name()));
    }
}
