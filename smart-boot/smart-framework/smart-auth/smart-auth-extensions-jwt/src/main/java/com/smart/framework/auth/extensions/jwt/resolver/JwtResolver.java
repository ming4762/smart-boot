package com.smart.framework.auth.extensions.jwt.resolver;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
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
    RestUserDetails resolver(@NonNull String jwt);

    /**
     * 创建JWT
     * @param userDetails 用户信息
     * @param effective 有效时间
     * @param payload 载荷信息
     * @return jwt字符串
     */
    String create(@NonNull RestUserDetails userDetails, @NonNull Duration effective, @Nullable Object payload);
}
