package com.smart.cloud.auth.event;

import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.cloud.api.system.feign.RemoteSysLogApi;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * 登录日志远程保存
 * @author zhongming4762
 * 2023/3/11
 */
@Component
public class RemoteAuthEventLogHandler implements AuthEventHandler {

    private final SysLogApi sysLogApi;

    public RemoteAuthEventLogHandler(RemoteSysLogApi sysLogApi) {
        this.sysLogApi = sysLogApi;
    }

    /**
     * 登录成功事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogSaveDTO dto = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGIN)
                .operation(LogSourceEnum.LOGIN.name())
                .result(String.format("登录成功,username:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .build();
        this.sysLogApi.saveLog(dto);
    }

    /**
     * 登出成功事件
     *
     * @param event 事件
     */
    @Override
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogSaveDTO dto = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGOUT)
                .operation(LogSourceEnum.LOGOUT.name())
                .result(String.format("登出成功,用户名:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .build();
        this.sysLogApi.saveLog(dto);
    }

    /**
     * 登录发生错误事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginFail(AbstractAuthenticationFailureEvent event) {
        AuthenticationException exception = event.getException();
        RestUsernamePasswordAuthenticationToken token = (RestUsernamePasswordAuthenticationToken) event.getAuthentication();
        SysLogSaveDTO dto = SysLogSaveDTO.builder()
                .ip(token.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .logSource(LogSourceEnum.LOGIN_FAIL)
                .operation(LogSourceEnum.LOGIN_FAIL.name())
                .result(String.format("%s[%s],username:[%s]", exception.getClass().getSimpleName(), exception.getMessage(), token.getPrincipal()))
                .build();

        this.sysLogApi.saveLog(dto);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
