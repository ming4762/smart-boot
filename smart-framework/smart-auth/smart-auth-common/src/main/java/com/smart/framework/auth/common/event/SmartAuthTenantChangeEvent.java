package com.smart.framework.auth.common.event;

import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;
import com.smart.framework.commons.core.event.AbstractSmartCommonEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 租户变更事件
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-15 18:03
 * @since 5.0.0
 */
@Getter
@NoArgsConstructor
@Setter
public class SmartAuthTenantChangeEvent extends AbstractSmartCommonEvent {
    
    private RestUserDetailsImpl oldUser;
    
    public SmartAuthTenantChangeEvent(AuthenticationTenantChangeEvent event) {
        this.oldUser = (RestUserDetailsImpl) event.getOldAuthentication().getPrincipal();
    }
}
