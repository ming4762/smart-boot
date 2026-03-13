package com.smart.cloud.starter.log.handler;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.SmartSaveLogEvent;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.framework.log.handler.LogHandler;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 远程调用保存日志信息
 * @author zhongming4762
 * 2023/3/11
 */
@RequiredArgsConstructor
public class RemoteLogHandler implements LogHandler {

   private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 保存日志
     *
     * @param sysLog        日志信息
     * @param point         切点
     * @param logAnnotation 日志注解
     * @param time          用户
     * @param code          返回编码
     * @param result        返回值
     * @param errorMessage  错误信息
     * @return 是否保存成功
     */
    @Override
    public boolean save(@NonNull SysLogSaveDTO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SmartSaveLogEvent saveLogEvent = new SmartSaveLogEvent();
        saveLogEvent.setLogData(sysLog);
        applicationEventPublisher.publishEvent(saveLogEvent);
        return true;
    }
}
