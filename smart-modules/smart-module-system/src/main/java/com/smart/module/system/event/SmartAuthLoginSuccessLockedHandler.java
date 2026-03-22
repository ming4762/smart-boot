package com.smart.module.system.event;

import com.smart.framework.auth.common.event.SmartAuthAuthenticationSuccessEvent;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.event.SmartEventHandler;
import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.AccountLoginFailTimeUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 登录成功处理
 * 处理用户锁定相关逻辑
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 19:02
 * @since 5.0.0
 */
@Component
@RequiredArgsConstructor
public class SmartAuthLoginSuccessLockedHandler implements SmartEventHandler<SmartAuthAuthenticationSuccessEvent> {

    private final SysUserApi sysUserApi;

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    public void handle(SmartAuthAuthenticationSuccessEvent event) {
        RestUserDetails user = event.getRestUserDetails();

        this.sysUserApi.updateLoginFailTime(new AccountLoginFailTimeUpdateDTO(user.getUsername(), 0L, user.getUserTenant().getTenantId()));
    }
}
