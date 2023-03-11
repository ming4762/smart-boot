package com.smart.system.log;

import com.smart.commons.core.log.Log;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.starter.log.handler.LogHandler;
import com.smart.starter.log.model.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanUtils;
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
public class DbLogHandler implements LogHandler {

    private final SysLogApi sysLogApi;

    public DbLogHandler(SysLogApi sysLogApi) {
        this.sysLogApi = sysLogApi;
    }

    @Override
    public boolean save(@NonNull SysLog sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SysLogSaveDTO dto = new SysLogSaveDTO();
        BeanUtils.copyProperties(sysLog, dto);
        return this.sysLogApi.saveLog(dto);
    }
}
