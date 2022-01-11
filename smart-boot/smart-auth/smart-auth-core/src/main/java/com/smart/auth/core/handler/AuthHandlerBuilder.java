package com.smart.auth.core.handler;

import lombok.Getter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.List;

/**
 * handler 构建器
 * @author shizhongming
 * 2021/1/2 8:58 下午
 */
public class AuthHandlerBuilder {

    @Getter
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Getter
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Getter
    private LogoutSuccessHandler logoutSuccessHandler;

    @Getter
    private List<LogoutHandler> logoutHandlers;

    /**
     * 设置登录成功执行器
     * @param authenticationSuccessHandler 登录成功
     * @return this
     */
    public AuthHandlerBuilder authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        return this;
    }

    /**
     * 设置登出handlers
     * @param logoutHandlers  logoutHandlers
     * @return this
     */
    public AuthHandlerBuilder logoutHandlers(List<LogoutHandler> logoutHandlers) {
        this.logoutHandlers = logoutHandlers;
        return this;
    }

    /**
     * 设置登录失败handler
     * @param authenticationFailureHandler 登录失败handler
     * @return this
     */
    public AuthHandlerBuilder authenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        return this;
    }

    /**
     * 设置登出成功handler
     * @param logoutSuccessHandler  登出成功handler
     * @return this
     */
    public AuthHandlerBuilder logoutSuccessHandler(LogoutSuccessHandler logoutSuccessHandler) {
        this.logoutSuccessHandler = logoutSuccessHandler;
        return this;
    }
}
