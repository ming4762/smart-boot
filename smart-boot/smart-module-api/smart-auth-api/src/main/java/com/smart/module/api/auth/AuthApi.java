package com.smart.module.api.auth;

import com.smart.commons.core.message.Result;
import com.smart.module.api.auth.dto.AuthCacheDTO;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import com.smart.module.api.auth.dto.AuthenticationDTO;
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

    /**
     * 通过token查询用户信息
     * @param token token
     * @return AuthUserDetailsDTO
     */
    AuthUserDetailsDTO getUserDetails(@NonNull String token);

    /**
     * 用户鉴权
     * @param parameter 验证的参数
     * @return 验证结果
     */
    Result<Boolean> authenticate(AuthenticationDTO parameter);

    /**
     * 获取认证缓存
     * @param key key
     * @return 缓存对象
     */
    Object getAuthCache(@NonNull String key);

    /**
     * 设置缓存信息
     * @param parameter key
     */
    void setAuthCache(@NonNull AuthCacheDTO parameter);

    /**
     * 删除缓存
     * @param key 缓存的key
     */
    void removeAuthCache(@NonNull String key);

}
