package com.smart.framework.auth.extensions.jwt.data;

import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;

import java.time.Instant;

/**
 * jwt解析器数据
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-03 16:44
 * @since 5.0.0
 */
public record JwtTokenResolverUser(
        // 用户信息
        RestUserDetailsImpl restUserDetails,
        // 签发时间
        Instant issuedAt,
        // 过期时间
        Instant expiresAt) {

}
