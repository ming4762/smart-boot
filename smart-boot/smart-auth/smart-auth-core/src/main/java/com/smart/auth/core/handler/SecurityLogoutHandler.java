package com.smart.auth.core.handler;

import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * 认证登出handler
 * 系统中需要添加 LogoutSuccessEventPublishingLogoutHandler 用于实现登出通知
 * 为了实现可自定义LogoutHandler（基于ConditionalOnMissingBean实现），添加该类用于区分 LogoutHandler 和 SecurityLogoutHandler
 * 防止因为存在LogoutSuccessEventPublishingLogoutHandler导致登出 LogoutHandler无法注入
 * @author ShiZhongMing
 * 2021/12/28
 * @since 1.0
 */
public interface SecurityLogoutHandler extends LogoutHandler {
}
