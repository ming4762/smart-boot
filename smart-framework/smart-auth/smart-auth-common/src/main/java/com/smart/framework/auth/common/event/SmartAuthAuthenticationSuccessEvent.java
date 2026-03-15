package com.smart.framework.auth.common.event;

import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;
import com.smart.framework.commons.core.event.AbstractSmartCommonEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

/**
 * 认证成功事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 16:28
 * @since 5.0.0
 */
@Getter
@NoArgsConstructor
@Setter
public class SmartAuthAuthenticationSuccessEvent extends AbstractSmartCommonEvent {

    private RestUserDetailsImpl restUserDetails;

    public SmartAuthAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        this.restUserDetails = (RestUserDetailsImpl) event.getAuthentication().getPrincipal();
    }
}
