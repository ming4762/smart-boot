package com.smart.framework.tool.database.exception;

import com.smart.framework.tool.database.constants.ExceptionConstant;

import java.io.Serial;

/**
 * @author shizhongming
 * 2020/1/19 8:21 下午
 */
public class SmartDatabaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2339582348409153072L;

    public SmartDatabaseException(ExceptionConstant exceptionConstant, Object ...args) {
        super(String.format(exceptionConstant.getValue(), args));
    }
}
