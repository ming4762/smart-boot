package com.smart.framework.document.exception;

import com.smart.framework.document.constants.DocumentFormatEnum;

import java.io.Serial;

/**
 * 不支持的转换源格式
 * @author ShiZhongMing
 * 2021/8/27 11:01
 * @since 1.0
 */
public class ConvertSourceFormatNotSupportException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6581836349539514014L;

    public ConvertSourceFormatNotSupportException(String message) {
        super(message);
    }

    public ConvertSourceFormatNotSupportException(DocumentFormatEnum sourceFormat) {
        super(String.format("source com.smart.framework.tool.code.type is not support: %s", sourceFormat.name()));
    }
}
