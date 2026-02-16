package com.smart.framework.auth.extensions.jwt.resolver;

import com.smart.framework.auth.extensions.jwt.data.JwtRefreshTokenPayload;
import com.smart.framework.auth.extensions.jwt.data.JwtTokenResolverUser;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Duration;

/**
 * JWT解析器
 * @author ShiZhongMing
 * 2021/5/26 13:48
 * @since 1.0
 */
public interface JwtResolver extends Ordered {

    /**
     * 解析JWT
     * @param jwt jwt
     * @return 解析结果
     */
    JwtTokenResolverUser resolverToken(@NonNull String jwt);

    /**
     * 解析刷新token
     * @param refreshToken 刷新token
     * @return 解析结果
     */
    JwtRefreshTokenPayload resolverRefreshToken(@NonNull String refreshToken);

    /**
     * 创建JWT
     * @param userId 用户ID
     * @param effective 有效时间
     * @param payload 载荷信息
     * @return jwt字符串
     */
    String create(@NonNull Long userId, @NonNull Duration effective, @Nullable Object payload);
}
