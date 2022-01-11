package com.smart.auth.extensions.jwt.store;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

/**
 * JWT 存储器
 * @author ShiZhongMing
 * 2021/12/29
 * @since 1.0
 */
public interface JwtStore extends Ordered {

    /**
     * 保存JWT
     * @param jwt jwt
     * @param user 用户信息
     * @return 是否保存成功
     */
    boolean save(@NonNull String jwt, @NonNull RestUserDetails user);

    /**
     * 验证JWT
     * @param jwt jwt
     * @param user 用户信息
     * @return 验证结果
     */
    boolean validate(@NonNull String jwt, @NonNull RestUserDetails user);
}
