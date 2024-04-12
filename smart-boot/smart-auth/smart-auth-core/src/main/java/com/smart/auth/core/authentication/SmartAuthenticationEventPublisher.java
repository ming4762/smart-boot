package com.smart.auth.core.authentication;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;

/**
 * 3.0.0
 * @author shizhongming
 * 2024/4/12 14:49
 * @since 3.0.0
 */
public interface SmartAuthenticationEventPublisher extends AuthenticationEventPublisher {

    /**
     * 发布租户切换实践
     * @param oldAuth 旧的认证信息
     * @param newAuth 新的认证信息
     */
    void publishTenantChange(Authentication oldAuth, Authentication newAuth);
}
