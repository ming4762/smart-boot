package com.smart.module.system.log;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.event.SmartAuthAuthenticationFailureEvent;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.framework.commons.core.log.LogSourceEnum;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.module.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 登录失败事件处理
 * 记录登录失败日志
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 18:12
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class LogAuthAuthenticationFailEventHandler implements SmartEventHandler<SmartAuthAuthenticationFailureEvent> {

    private final SysLogService sysLogService;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    public void handle(SmartAuthAuthenticationFailureEvent event) {
        String exceptionClass = event.getExceptionClass();
        String exceptionMessage = event.getExceptionMessage();
        String loginIp = event.getLoginIp();
        AuthTypeEnum authType = event.getAuthType();

        SysLogSaveDTO sysLog = SysLogSaveDTO.builder()
                .ip(loginIp)
                .ident(LogIdentEnum.LOGIN_LOG.getValue())
                .statusCode(org.springframework.http.HttpStatus.UNAUTHORIZED.value())
                .logSource(LogSourceEnum.LOGIN_FAIL)
                .operation(authType == null ? null : authType.name())
                .result(String.format("%s[%s],username:[%s]", exceptionClass, exceptionMessage, event.getUsername()))
                .build();
        sysLogService.saveLog(sysLog);
    }
}
