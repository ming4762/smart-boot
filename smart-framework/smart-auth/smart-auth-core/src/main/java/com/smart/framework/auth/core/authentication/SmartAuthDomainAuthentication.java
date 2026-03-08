package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.constants.AuthDomainConstants;
import org.springframework.security.core.Authentication;

/**
 * 认证域认证令牌接口
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/3/8 20:31
 * @since 5.0.0
 */
public interface SmartAuthDomainAuthentication extends Authentication {

    /**
     * 获取认证域
     * @return 认证域
     */
    String getAuthDomain();

    /**
     * 是否为非认证域
     * @return 是否为非认证域
     */
    default boolean isNonAuthDomain() {
        String authDomain = getAuthDomain();
        return authDomain == null || AuthDomainConstants.AUTH_DOMAIN_NONE.equals(authDomain);
    }
}
