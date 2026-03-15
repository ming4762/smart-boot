package com.smart.module.system.log;

import com.smart.framework.auth.common.event.SmartAuthLogoutSuccessEvent;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.framework.commons.core.log.LogSourceEnum;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.module.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 登出成功事件
 * 保存登出成功日志
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 18:11
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class LogAuthLogoutSuccessEventHandler implements SmartEventHandler<SmartAuthLogoutSuccessEvent> {

    private final SysLogService sysLogService;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    public void handle(SmartAuthLogoutSuccessEvent event) {
        RestUserDetails user = event.getRestUserDetails();
        SysLogSaveDTO sysLog = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG.getValue())
                .statusCode(org.springframework.http.HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGOUT)
                .operation(LogSourceEnum.LOGOUT.name())
                .result(String.format("登出成功,用户名:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .createUserId(user.getUserId())
                .createBy(user.getFullName())
                .tenantId(user.getUserTenant().getTenantId())
                .build();
        this.sysLogService.saveLog(sysLog);
    }
}
