package com.smart.starter.log.handler;

import com.smart.commons.core.log.Log;
import com.smart.starter.log.model.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author ShiZhongMing
 * 2021/4/22 13:46
 * @since 1.0
 */
public class DefaultLogHandler implements LogHandler {
    @Override
    public boolean save(@NonNull SysLog sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        return true;
    }
}
