package com.smart.framework.auth.extensions.jwt.data;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.commons.core.dto.auth.UserTenantDTO;

/**
 * JWT refresh token payload
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/2/3 14:38
 * @since 5.0.0
 */
public record JwtRefreshTokenPayload(String username, UserTenantDTO userTenant) {

    public static JwtRefreshTokenPayload createByUser(RestUserDetails user) {
        return new JwtRefreshTokenPayload(user.getUsername(), user.getUserTenant());
    }
}
