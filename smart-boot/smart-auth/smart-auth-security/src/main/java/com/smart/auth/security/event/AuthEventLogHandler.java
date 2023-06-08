package com.smart.auth.security.event;

import com.smart.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.http.HttpStatus;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.module.api.system.dto.SysLogSaveDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.AuthenticationException;

/**
 * 保存登录日志
 * @author zhongming4762
 * 2023/6/7
 */
public class AuthEventLogHandler implements AuthEventHandler {

    private final SysLogApi sysLogApi;

    public AuthEventLogHandler(SysLogApi sysLogApi) {
        this.sysLogApi =sysLogApi;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * 登录成功事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogSaveDTO log = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.OK.getCode())
                .logSource(LogSourceEnum.LOGIN)
                .operation(user.getAuthType().name())
                .result(String.format("登录成功,username:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .createUserId(user.getUserId())
                .createBy(user.getFullName())
                .build();
        this.sysLogApi.saveLog(log);
    }

    /**
     * 登出成功事件
     *
     * @param event 事件
     */
    @Override
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogSaveDTO sysLog = SysLogSaveDTO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(org.springframework.http.HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGOUT)
                .operation(LogSourceEnum.LOGOUT.name())
                .result(String.format("登出成功,用户名:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .createUserId(user.getUserId())
                .createBy(user.getFullName())
                .build();
        this.sysLogApi.saveLog(sysLog);
    }

    /**
     * 登录发生错误事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginFail(AbstractAuthenticationFailureEvent event) {
        AuthenticationException exception = event.getException();
        AbstractAuthenticationToken token = (AbstractAuthenticationToken) event.getAuthentication();
        String loginIp = "";
        AuthTypeEnum authType = null;
        if (token instanceof RestUsernamePasswordAuthenticationToken restToken) {
            loginIp = restToken.getLoginIp();
            authType = AuthTypeEnum.USERNAME;
        } else if (AbstractEnhanceAuthenticationToken.class.isAssignableFrom(token.getClass())) {
            loginIp = ((AbstractEnhanceAuthenticationToken) token).getLoginIp();
            authType = ((AbstractEnhanceAuthenticationToken) token).getAuthType();
        }
        SysLogSaveDTO sysLog = SysLogSaveDTO.builder()
                .ip(loginIp)
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(org.springframework.http.HttpStatus.UNAUTHORIZED.value())
                .logSource(LogSourceEnum.LOGIN_FAIL)
                .operation(authType == null ? null : authType.name())
                .result(String.format("%s[%s],username:[%s]", exception.getClass().getSimpleName(), exception.getMessage(), token.getPrincipal()))
                .build();
        this.sysLogApi.saveLog(sysLog);
    }
}
