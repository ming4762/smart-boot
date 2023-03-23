package com.smart.cloud.api.auth.feign;

import com.smart.cloud.common.core.constants.CloudServiceNameConstants;
import com.smart.commons.core.message.Result;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.constants.SmartAuthApiUrlConstants;
import com.smart.module.api.auth.dto.AuthCacheDTO;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 认证模块feign调用接口
 * @author zhongming4762
 * 2023/3/9
 */
@FeignClient(value = CloudServiceNameConstants.AUTH_SERVICE, contextId = "remoteAuthApi")
public interface RemoteAuthApi extends AuthApi {


    /**
     * 通过token离线
     *
     * @param token token
     * @return 结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.OFFLINE_BY_TOKEN)
    boolean offlineByToken(@NonNull String token);

    /**
     * 通过用户名离线
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.OFFLINE_BY_USERNAME)
    boolean offlineByUsername(@NonNull String username);

    /**
     * 通过token查询用户信息
     *
     * @param token token
     * @return AuthUserDetailsDTO
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.GET_USER_DETAILS_BY_TOKEN)
    AuthUserDetailsDTO getUserDetails(@NonNull String token);

    /**
     * 用户鉴权
     * @param parameter 验证的参数
     * @return 验证结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_AUTHENTICATE)
    Result<Boolean> authenticate(AuthenticationDTO parameter);


    /**
     * 获取认证缓存
     *
     * @param key key
     * @return 缓存对象
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.GET_AUTH_CACHE)
    Object getAuthCache(@NonNull String key);

    /**
     * 设置缓存信息
     * @param parameter 参数
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.SET_AUTH_CACHE)
    void setAuthCache(@NonNull AuthCacheDTO parameter);

    /**
     * 删除缓存
     *
     * @param key 缓存的key
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.REMOVE_AUTH_CACHE)
    void removeAuthCache(@NonNull String key);
}
