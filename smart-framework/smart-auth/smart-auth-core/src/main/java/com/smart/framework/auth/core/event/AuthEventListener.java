package com.smart.framework.auth.core.event;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.event.*;
import com.smart.framework.auth.core.authentication.AbstractEnhanceAuthenticationToken;
import com.smart.framework.auth.core.authentication.RestUsernamePasswordAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;

/**
 * 登录事件监听器
 * 将登录事件转为smart-event
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Slf4j
public class AuthEventListener implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 登录失败日志
     * @param event 登录失败事件
     */
    @EventListener(classes = AbstractAuthenticationFailureEvent.class)
    public void onLoginFail(AbstractAuthenticationFailureEvent event) {
        Authentication token = event.getAuthentication();
        String loginIp = "";
        AuthTypeEnum authType = null;
        if (token instanceof RestUsernamePasswordAuthenticationToken restToken) {
            loginIp = restToken.getLoginIp();
            authType = AuthTypeEnum.USERNAME;
        } else if (AbstractEnhanceAuthenticationToken.class.isAssignableFrom(token.getClass())) {
            loginIp = ((AbstractEnhanceAuthenticationToken) token).getLoginIp();
            authType = ((AbstractEnhanceAuthenticationToken) token).getAuthType();
        }
        SmartAuthAuthenticationFailureEvent failureEvent = new SmartAuthAuthenticationFailureEvent(event);
        failureEvent.setLoginIp(loginIp);
        failureEvent.setAuthType(authType);
        failureEvent.setUsername(token.getPrincipal().toString());
        this.applicationContext.publishEvent(failureEvent);
    }

    /**
     * 登录成功日志
     * @param event 登录成功事件
     */
    @EventListener(classes = AuthenticationSuccessEvent.class)
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        this.applicationContext.publishEvent(new SmartAuthAuthenticationSuccessEvent(event));
    }

    /**
     * 登录
     * @param event 登出成功事件
     */
    @EventListener(classes = LogoutSuccessEvent.class)
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        this.applicationContext.publishEvent(new SmartAuthLogoutSuccessEvent(event));
    }

    /**
     * 租户变更事件
     * @param event AuthenticationTenantChangeEvent
     */
    @EventListener(classes = AuthenticationTenantChangeEvent.class)
    public void onTenantChange(AuthenticationTenantChangeEvent event) {
        this.applicationContext.publishEvent(new SmartAuthTenantChangeEvent(event));
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
