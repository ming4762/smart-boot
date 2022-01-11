package com.smart.converter.office.exception;

/**
 * @author ShiZhongMing
 * 2021/8/26 10:19
 * @since 1.0
 */
public class ConvertTypeNotSupportException extends RuntimeException {
    private static final long serialVersionUID = -5083926153614157337L;

    public ConvertTypeNotSupportException() {
        super();
    }

    public ConvertTypeNotSupportException(String message) {
        super(message);
    }

    public ConvertTypeNotSupportException(String fileType, String supportFileType) {
        this(String.format("convert type not support，support type is [%s], the type is [%s]", supportFileType, fileType));
    }
}
