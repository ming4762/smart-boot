package com.smart.system.auth;

import com.smart.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import com.smart.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.log.LogSourceEnum;
import com.smart.module.api.system.constants.LogIdentEnum;
import com.smart.system.model.SysLogPO;
import com.smart.system.service.SysLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.AuthenticationException;

/**
 * 保存登录日志
 * @author ShiZhongMing
 * 2021/12/30 14:23
 * @since 1.0
 */
public class AuthEventLogHandler implements AuthEventHandler {

    private final SysLogService sysLogService;

    public AuthEventLogHandler(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @Override
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogPO sysLog = SysLogPO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGIN)
                .operation(user.getAuthType().name())
                .result(String.format("登录成功,username:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .build();
        sysLog.setCreateUserId(user.getUserId());
        sysLog.setCreateBy(user.getFullName());
        this.sysLogService.save(sysLog);
    }

    @Override
    public void handleLogoutSuccess(LogoutSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        SysLogPO sysLog = SysLogPO.builder()
                .ip(user.getLoginIp())
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.OK.value())
                .logSource(LogSourceEnum.LOGOUT)
                .operation(LogSourceEnum.LOGOUT.name())
                .result(String.format("登出成功,用户名:[%s],fullName:[%s]", user.getUsername(), user.getFullName()))
                .build();
        this.sysLogService.save(sysLog);
    }

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
        SysLogPO sysLog = SysLogPO.builder()
                .ip(loginIp)
                .ident(LogIdentEnum.LOGIN_LOG)
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .logSource(LogSourceEnum.LOGIN_FAIL)
                .operation(authType == null ? null : authType.name())
                .result(String.format("%s[%s],username:[%s]", exception.getClass().getSimpleName(), exception.getMessage(), token.getPrincipal()))
                .build();
        this.sysLogService.save(sysLog);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
