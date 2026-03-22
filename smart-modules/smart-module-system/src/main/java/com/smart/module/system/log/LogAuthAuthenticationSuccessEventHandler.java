package com.smart.module.system.log;

import com.smart.framework.auth.common.event.SmartAuthAuthenticationSuccessEvent;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.framework.commons.core.http.HttpStatus;
import com.smart.framework.commons.core.log.LogSourceEnum;
import com.smart.framework.commons.core.log.SysLogSaveDTO;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.module.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 认证成功事件
 * 保存登录成功日志
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 18:08
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class LogAuthAuthenticationSuccessEventHandler implements SmartEventHandler<SmartAuthAuthenticationSuccessEvent> {

    private final SysLogService sysLogService;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SmartAuthAuthenticationSuccessEvent event) {
        RestUserDetails user = event.getRestUserDetails();
        SysLogSaveDTO log = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG.getValue())
                .statusCode(HttpStatus.OK.getCode())
                .logSource(LogSourceEnum.LOGIN)
                .operation(Optional.ofNullable(user.getAuthType()).map(Enum::name).orElse(null))
                .result(String.format("登录成功,username:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .createUserId(user.getUserId())
                .createBy(user.getFullName())
                .tenantId(user.getUserTenant().getTenantId())
                .build();
        sysLogService.saveLog(log);
    }
}
