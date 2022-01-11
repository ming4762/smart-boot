package com.smart.system.log;

import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.log.Log;
import com.smart.starter.log.handler.LogHandler;
import com.smart.starter.log.model.SysLog;
import com.smart.system.model.SysLogPO;
import com.smart.system.service.SysLogService;
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

    private final SysLogService sysLogService;

    public DbLogHandler(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @Override
    public boolean save(@NonNull SysLog sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SysLogPO po = new SysLogPO();
        BeanUtils.copyProperties(sysLog, po);
        this.sysLogService.saveWithUser(po, AuthUtils.getCurrentUserId());
        return true;
    }
}
