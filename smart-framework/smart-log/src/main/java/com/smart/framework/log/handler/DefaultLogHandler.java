package com.smart.framework.log.handler;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
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
    public boolean save(@NonNull SysLogSaveDTO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        return true;
    }
}
