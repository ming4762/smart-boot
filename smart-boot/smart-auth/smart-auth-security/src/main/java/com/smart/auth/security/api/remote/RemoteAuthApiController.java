package com.smart.auth.security.api.remote;

import com.smart.auth.security.api.local.LocalAuthApiImpl;
import com.smart.commons.core.message.Result;
import com.smart.module.api.auth.AuthApi;
import com.smart.module.api.auth.constants.SmartAuthApiUrlConstants;
import com.smart.module.api.auth.dto.AuthCacheDTO;
import com.smart.module.api.auth.dto.AuthUserDetailsDTO;
import com.smart.module.api.auth.dto.AuthenticationDTO;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证模块远程调用接口
 * @author zhongming4762
 * 2023/3/9
 */
@RestController
@RequestMapping
public class RemoteAuthApiController implements AuthApi {

    private final LocalAuthApiImpl localAuthApi;

    public RemoteAuthApiController(LocalAuthApiImpl localAuthApi) {
        this.localAuthApi = localAuthApi;
    }

    /**
     * 通过token离线
     *
     * @param token token
     * @return 结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.OFFLINE_BY_TOKEN)
    public boolean offlineByToken(@RequestBody @NonNull String token) {
        return this.localAuthApi.offlineByToken(token);
    }

    /**
     * 通过用户名离线
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.OFFLINE_BY_USERNAME)
    public boolean offlineByUsername(@RequestBody @NonNull String username) {
        return this.localAuthApi.offlineByUsername(username);
    }

    /**
     * 通过token查询用户信息
     *
     * @param token token
     * @return AuthUserDetailsDTO
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.GET_USER_DETAILS_BY_TOKEN)
    public AuthUserDetailsDTO getUserDetails(@NonNull @RequestBody String token) {
        return this.localAuthApi.getUserDetails(token);
    }

    /**
     * 用户鉴权
     *
     * @param parameter 验证的参数
     * @return 验证结果
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_AUTHENTICATE)
    public Result<Boolean> authenticate(@RequestBody AuthenticationDTO parameter) {
        return this.localAuthApi.authenticate(parameter);
    }

    /**
     * 获取认证缓存
     *
     * @param key key
     * @return 缓存对象
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.GET_AUTH_CACHE)
    public Object getAuthCache(@NonNull @RequestBody String key) {
        return this.localAuthApi.getAuthCache(key);
    }

    /**
     * 设置缓存信息
     *
     * @param parameter 参数
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.SET_AUTH_CACHE)
    public void setAuthCache(@NonNull @RequestBody AuthCacheDTO parameter) {
        this.localAuthApi.setAuthCache(parameter);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存的key
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.REMOVE_AUTH_CACHE)
    public void removeAuthCache(@NonNull @RequestBody String key) {
        this.localAuthApi.removeAuthCache(key);
    }
}
