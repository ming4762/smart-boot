package com.smart.framework.auth.common.event;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;
import com.smart.framework.commons.core.event.AbstractSmartCommonEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.AuthenticationException;

/**
 * 认证失败事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 16:31
 * @since 5.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class SmartAuthAuthenticationFailureEvent extends AbstractSmartCommonEvent {

    private String exceptionClass;
    private String exceptionMessage;
    private boolean isLockedEvent;
    private RestUserDetailsImpl restUserDetails;

    private String username;
    private String loginIp;
    private AuthTypeEnum authType;

    public SmartAuthAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
        AuthenticationException exception = event.getException();
        this.exceptionClass = exception.getClass().getName();
        this.exceptionMessage = exception.getMessage();
        this.isLockedEvent = event instanceof AuthenticationFailureLockedEvent;
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof RestUserDetailsImpl userDetails) {
            this.restUserDetails = userDetails;
        } else {
            restUserDetails = null;
        }
    }
}
