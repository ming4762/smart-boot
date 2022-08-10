package com.smart.auth.core.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * 登录事件监听器
 * @author ShiZhongMing
 * 2021/12/30
 * @since 1.0.7
 */
@Slf4j
public class AuthEventListener implements ApplicationContextAware {

    private List<AuthEventHandler> handlerList;

    public AuthEventListener() {
        this.handlerList = new ArrayList<>(0);
    }

    /**
     * 登录失败日志
     * @param event 登录失败事件
     */
    @EventListener(classes = AbstractAuthenticationFailureEvent.class)
    public void onLoginFail(AbstractAuthenticationFailureEvent event) {
        this.handler(handler -> handler.handleLoginFail(event));
    }

    /**
     * 登录成功日志
     * @param event 登录成功事件
     */
    @EventListener(classes = AuthenticationSuccessEvent.class)
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        this.handler(handler -> handler.handleLoginSuccess(event));
    }

    /**
     * 登录
     * @param event 登出成功事件
     */
    @EventListener(classes = LogoutSuccessEvent.class)
    public void onLogoutSuccess(LogoutSuccessEvent event) {
        this.handler(handler -> handler.handleLogoutSuccess(event));
    }

    private void handler(Consumer<AuthEventHandler> handlerConsumer) {
        for (AuthEventHandler handler : this.handlerList) {
            try {
                handlerConsumer.accept(handler);
            } catch (Exception e) {
                log.error("触发事件失败", e);
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.handlerList = Arrays.stream(applicationContext.getBeanNamesForType(AuthEventHandler.class))
                .map(item -> applicationContext.getBean(item, AuthEventHandler.class))
                .sorted(Comparator.comparing(AuthEventHandler::getOrder))
                .toList();
    }
}
