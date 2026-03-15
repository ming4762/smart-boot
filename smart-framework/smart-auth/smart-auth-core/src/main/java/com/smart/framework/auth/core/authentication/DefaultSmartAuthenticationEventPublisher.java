package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.event.AuthenticationTenantChangeEvent;
import lombok.Setter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.core.Authentication;

/**
 * @author shizhongming
 * 2024/4/12 14:52
 * @since 3.0.0
 */
@Setter
public class DefaultSmartAuthenticationEventPublisher extends DefaultAuthenticationEventPublisher implements SmartAuthenticationEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public DefaultSmartAuthenticationEventPublisher() {
        this(null);
    }

    public DefaultSmartAuthenticationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 发布租户切换实践
     *
     * @param oldAuth 旧的认证信息
     * @param newAuth 新的认证信息
     */
    @Override
    public void publishTenantChange(Authentication oldAuth, Authentication newAuth) {
        if (this.applicationEventPublisher != null) {
            this.applicationEventPublisher.publishEvent(new AuthenticationTenantChangeEvent(oldAuth, newAuth));
        }
    }
}
