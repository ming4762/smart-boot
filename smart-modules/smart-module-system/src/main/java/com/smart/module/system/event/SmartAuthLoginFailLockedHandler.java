package com.smart.module.system.event;

import com.smart.framework.auth.common.event.SmartAuthAuthenticationFailureEvent;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.framework.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import com.smart.module.api.system.dto.UserAccountLockDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 登录失败锁定处理
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 19:04
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SmartAuthLoginFailLockedHandler implements SmartEventHandler<SmartAuthAuthenticationFailureEvent> {

    private static final List<String> LOCK_EXCEPTION_CLASS = List.of(
            LongTimeNoLoginLockedException.class.getName(),
            PasswordNoLifeLockedException.class.getName()
    );

    private final SysUserApi sysUserApi;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    public void handle(SmartAuthAuthenticationFailureEvent event) {
        if (event.isLockedEvent()) {
            this.handleLocked(event);
            return;
        }
        if (BadCredentialsException.class.getName().equals(event.getExceptionClass())) {
            RestUserDetails restUserDetails = event.getRestUserDetails();
            if (restUserDetails != null) {
                this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO(restUserDetails.getUsername(), 1L, restUserDetails.getUserTenant().getTenantId()));
            } else {
                log.error("记录登录失败发生错误，无法获取用户信息，错误信息：{}", event.getExceptionMessage());
            }
        }
    }

    private void handleLocked(SmartAuthAuthenticationFailureEvent event) {
        String exceptionClass = event.getExceptionClass();
        if (!LOCK_EXCEPTION_CLASS.contains(exceptionClass)) {
            return;
        }

        UserAccountLockDTO parameter = new UserAccountLockDTO();
        parameter.setUsername(event.getUsername());
        if (exceptionClass.equals(LongTimeNoLoginLockedException.class.getName())) {
            // 长时间未登录锁定
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_LOCKED);
            parameter.setUsername(event.getUsername());
        } else {
            parameter.setUsername(event.getUsername());
            parameter.setAccountStatus(UserAccountStatusEnum.LONG_TIME_PASSWORD_MODIFY_LOCKED);
        }
        parameter.setTenantId(Objects.requireNonNull(SmartTenantHolder.getTenantId()));
        this.sysUserApi.lockAccount(parameter);
    }
}
