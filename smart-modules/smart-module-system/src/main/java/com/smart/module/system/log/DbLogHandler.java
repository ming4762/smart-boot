package com.smart.module.system.log;

import com.smart.framework.commons.core.log.Log;
import com.smart.framework.commons.core.log.SmartSaveLogEvent;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.framework.log.handler.LogHandler;
import com.smart.module.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 保存日志到数据库
 * @author ShiZhongMing
 * 2021/12/17
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class DbLogHandler implements LogHandler {

    private final SysLogService sysLogService;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 切面日志转为日志保存事件
     */
    @Override
    public boolean save(@NonNull SysLogSaveDTO sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SmartSaveLogEvent event = new SmartSaveLogEvent();
        event.setLogData(sysLog);
        applicationEventPublisher.publishEvent(event);
        return true;
    }

    /**
     * 保存事件日志到数据库
     * @param event 事件
     */
    @EventListener(SmartSaveLogEvent.class)
    public void saveEventLog(SmartSaveLogEvent event) {
        SysLogSaveDTO dto = new SysLogSaveDTO();
        BeanUtils.copyProperties(event.getLogData(), dto);
        this.sysLogService.saveLog(dto);
    }
}
