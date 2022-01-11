package com.smart.auth.core.event;

import org.springframework.core.Ordered;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

/**
 * 认证事件接口
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
public interface AuthEventHandler extends Ordered {

    /**
     * 登录成功事件
     * @param event 事件
     */
    default void handleLoginSuccess(AuthenticationSuccessEvent event) {
        // Do Nothing
    }

    /**
     * 登出成功事件
     * @param event 事件
     */
    default void handleLogoutSuccess(LogoutSuccessEvent event) {
        // Do Nothing
    }

    /**
     * 登录发生错误事件
     * @param event 事件
     */
    default void handleLoginFail(AbstractAuthenticationFailureEvent event) {
        // Do Nothing
    }
}
