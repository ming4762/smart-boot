package com.smart.auth.extensions.jwt.store;

import org.springframework.lang.NonNull;

/**
 * jwt缓存存储器
 * @author ShiZhongMing
 * 2022/1/17
 * @since 2.0.0
 */
public interface CacheJwtStore extends JwtStore {

    /**
     * 通过token失效
     * @param token token
     * @param username 用户名
     * @return 是否成功
     */
    boolean invalidateByToken(@NonNull String username, @NonNull String token);

    /**
     * 使用户登录失效
     * @param username 用户名
     * @return 是否成功
     */
    boolean invalidateByUsername(@NonNull String username);
}
