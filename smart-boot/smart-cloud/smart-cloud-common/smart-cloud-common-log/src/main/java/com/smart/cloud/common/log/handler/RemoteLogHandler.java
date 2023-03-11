package com.smart.cloud.common.log.handler;

import com.smart.cloud.api.system.feign.RemoteSysLogApi;
import com.smart.commons.core.log.Log;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import com.smart.starter.log.handler.LogHandler;
import com.smart.starter.log.model.SysLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 远程调用保存日志信息
 * @author zhongming4762
 * 2023/3/11
 */
public class RemoteLogHandler implements LogHandler {

    private final RemoteSysLogApi logApi;

    public RemoteLogHandler(RemoteSysLogApi logApi) {
        this.logApi = logApi;
    }

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
    public boolean save(@NonNull SysLog sysLog, @NonNull ProceedingJoinPoint point, @NonNull Log logAnnotation, long time, int code, @Nullable Object result, @Nullable String errorMessage) {
        SysLogSaveDTO parameter = new SysLogSaveDTO();
        BeanUtils.copyProperties(sysLog, parameter);
        return this.logApi.saveLog(parameter);
    }
}
