package com.smart.auth.core.secret.store;

import com.smart.auth.core.secret.data.AccessTokenStoreData;
import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/11/1 11:34
 * @since 1.0
 */
public interface AccessTokenStore {

    /**
     * 存储access token
     * @param app app 信息
     * @param accessToken accessToken
     */
    void save(@NonNull RestUserDetails app, @NonNull String accessToken);

    /**
     * 验证accessToken 有效性
     * @param appId appId
     * @return accessToken 是否有效
     */
    AccessTokenStoreData validate(@NonNull String appId);
}
