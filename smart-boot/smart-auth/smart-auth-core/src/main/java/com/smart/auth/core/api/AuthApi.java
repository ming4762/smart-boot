package com.smart.auth.core.api;

import org.springframework.lang.NonNull;

/**
 * 认证模块API
 * 提供认证模块接口处理
 * @author zhongming4762
 * 2023/3/7
 */
public interface AuthApi {

    /**
     * 通过token离线
     * @param token token
     * @return 结果
     */
    boolean offlineByToken(@NonNull String token);

    /**
     * 通过用户名离线
     * @param username 用户名
     * @return 结果
     */
    boolean offlineByUsername(@NonNull String username);
}
