package com.smart.framework.log.handler;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.SmartSaveLogEvent;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author ShiZhongMing
 * 2021/4/22 13:46
 * @since 1.0
 */
@RequiredArgsConstructor
public class DefaultLogHandler implements LogHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public boolean save(@NonNull SysLogSaveDTO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SmartSaveLogEvent event = new SmartSaveLogEvent();
        event.setLogData(sysLog);
        applicationEventPublisher.publishEvent(event);
        return true;
    }
}
