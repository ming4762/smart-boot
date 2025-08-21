package com.smart.framework.tool.database.exception;

/**
 * SQL检查异常
 * @author shizhongming
 * 2025/8/19 19:21
 * @since 5.0.0
 */
public class SmartSqlCheckerException extends RuntimeException {
    public SmartSqlCheckerException(String message) {
        super(message);
    }
    public SmartSqlCheckerException(String message, Throwable cause) {
        super(message, cause);
    }
}
