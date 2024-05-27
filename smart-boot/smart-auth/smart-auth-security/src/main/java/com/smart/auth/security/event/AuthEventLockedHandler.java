package com.smart.auth.security.event;

import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhongming4762
 * 2023/3/11 19:55
 */
public class AuthEventLockedHandler implements AuthEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthEventLockedHandler.class);
    private final SysUserApi sysUserApi;

    public AuthEventLockedHandler(SysUserApi sysUserApi) {
        this.sysUserApi = sysUserApi;
    }

    /**
     * 登录成功事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO(user.getUsername(), 0L, user.getUserTenant().getTenantId()));
    }

    /**
     * 登录发生错误事件
     *
     * @param event 事件
     */
    @Override
    public void handleLoginFail(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureLockedEvent event1) {
            this.handleLocked(event1);
            return;
        }
        if (event.getException() instanceof BadCredentialsException) {
            Object details = event.getAuthentication().getDetails();
            if (details instanceof RestUserDetails restUserDetails) {
                this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO(restUserDetails.getUsername(), 1L, restUserDetails.getUserTenant().getTenantId()));
            } else {
                log.error("记录登录失败发生错误，无法获取用户信息", event.getException());
            }
        }
    }

    private void handleLocked(AuthenticationFailureLockedEvent event) {
        AuthenticationException exception = event.getException();
        if (!(exception instanceof LongTimeNoLoginLockedException || exception instanceof PasswordNoLifeLockedException)) {
            return;
        }
        UserAccountLockDTO parameter = new UserAccountLockDTO();
        parameter.setUsername((String) event.getAuthentication().getPrincipal());
        if (exception instanceof LongTimeNoLoginLockedException longTimeNoLoginLockedException) {
            // 长时间未登录锁定
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_LOCKED);
            parameter.setUsername(longTimeNoLoginLockedException.getUser().getUsername());
        } else {
            parameter.setUsername(((PasswordNoLifeLockedException) exception).getUser().getUsername());
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_PASSWORD_MODIFY_LOCKED);
        }
        this.sysUserApi.lockAccount(parameter);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
