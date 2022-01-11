package com.smart.auth.extensions.appsecret.service;

import com.smart.auth.core.userdetails.RestUserDetails;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public interface AppSecretService {


    /**
     * 通过appId加载APP信息
     * @param appId APP ID
     * @return APP 信息
     */
    @Nullable
    RestUserDetails loadByAppId(@NonNull String appId);

}
