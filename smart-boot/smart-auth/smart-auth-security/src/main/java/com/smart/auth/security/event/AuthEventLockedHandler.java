package com.smart.auth.security.event;

import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.constants.UserAccountStatusEnum;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;

/**
 * @author zhongming4762
 * 2023/3/11 19:55
 */
public class AuthEventLockedHandler implements AuthEventHandler {

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
        this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO(user.getUsername(), 0L));
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
        this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO((String) event.getAuthentication().getPrincipal(), 1L));
    }

    private void handleLocked(AuthenticationFailureLockedEvent event) {
        AuthenticationException exception = event.getException();
        if (!(exception instanceof LongTimeNoLoginLockedException || exception instanceof PasswordNoLifeLockedException)) {
            return;
        }
        UserAccountLockDTO parameter = new UserAccountLockDTO();
        parameter.setUsername((String) event.getAuthentication().getPrincipal());
        if (exception instanceof LongTimeNoLoginLockedException) {
            // 长时间未登录锁定
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_LOCKED);
        } else {
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_PASSWORD_MODIFY_LOCKED);
        }
        this.sysUserApi.lockAccount(parameter);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
