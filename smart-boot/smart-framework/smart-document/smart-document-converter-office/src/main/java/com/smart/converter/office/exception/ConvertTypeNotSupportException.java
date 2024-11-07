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
        this(String.format("convert com.smart.framework.tool.code.type not support，support com.smart.framework.tool.code.type is [%s], the com.smart.framework.tool.code.type is [%s]", supportFileType, fileType));
    }
}
