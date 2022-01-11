package com.smart.document.exception;

import com.smart.document.constants.DocumentFormatEnum;

/**
 * 不支持的转换源格式
 * @author ShiZhongMing
 * 2021/8/27 11:01
 * @since 1.0
 */
public class ConvertSourceFormatNotSupportException extends RuntimeException {
    private static final long serialVersionUID = 6581836349539514014L;

    public ConvertSourceFormatNotSupportException(String message) {
        super(message);
    }

    public ConvertSourceFormatNotSupportException(DocumentFormatEnum sourceFormat) {
        super(String.format("source type is not support: %s", sourceFormat.name()));
    }
}
