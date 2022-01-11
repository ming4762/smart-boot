package com.smart.db.analysis.exception;

import com.smart.db.analysis.constants.ExceptionConstant;

/**
 * TODO：支持I18N
 * @author shizhongming
 * 2020/1/19 8:21 下午
 */
public class SmartDatabaseException extends RuntimeException {
    private static final long serialVersionUID = 2339582348409153072L;
    public SmartDatabaseException(ExceptionConstant exceptionConstant, Object ...args) {
        super(String.format(exceptionConstant.getValue(), args));
    }
}
