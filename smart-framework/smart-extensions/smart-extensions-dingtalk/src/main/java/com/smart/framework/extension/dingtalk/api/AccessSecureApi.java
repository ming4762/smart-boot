package com.smart.framework.extension.dingtalk.api;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.dingtalk.constants.DingtalkGrantTypeEnum;
import com.smart.framework.extension.dingtalk.exception.DingtalkApiException;
import com.smart.framework.extension.dingtalk.pojo.dto.GetAccessTokenResult;
import com.smart.framework.extension.dingtalk.pojo.parameter.AppKeySecretParameter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * 认证服务类
 * @author shizhongming
 * 2024/4/26 21:32
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class AccessSecureApi extends AbstractDingtalkApi {

    /**
     * token过期时间减200S，防止token过期
     */
    private static final Duration ACCESS_TOKEN_EXPIRES_DURATION = Duration.ofSeconds(200);
    private static final String KEY_PREFIX = "dingtalk_access_token_";

    private final CacheService cacheService;

    /**
     * 获取应用内部access token
     * @param parameter 参数
     * @return token
     */
    @SneakyThrows(Exception.class)
    public GetAccessTokenResult getInnerAppAccessToken(AppKeySecretParameter parameter) {
        GetAccessTokenResult accessTokenResult = this.cacheService.get(this.getCacheKey(parameter.getAppKey()));
        if (accessTokenResult != null) {
            return accessTokenResult;
        }
        Client client = this.createAuthClient();
        // 请求获取access token
        GetAccessTokenRequest accessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(parameter.getAppKey())
                .setAppSecret(parameter.getAppSecret());

        GetAccessTokenResponse response = client.getAccessToken(accessTokenRequest);
        GetAccessTokenResponseBody body = response.getBody();

        // 计算有效期
        Duration duration = Duration.ofSeconds(body.getExpireIn()).minus(ACCESS_TOKEN_EXPIRES_DURATION);
        ZonedDateTime expireAt = ZonedDateTime.now().plus(duration);

        GetAccessTokenResult result = new GetAccessTokenResult(body.accessToken, expireAt);
        // 设置缓存
        this.cacheService.put(this.getCacheKey(parameter.getAppKey()), result, duration);

        return result;
    }

    /**
     * 通过授权码获取用户的access token
     * @param authCode 授权码
     * @param parameter 参数
     * @return token
     */
    @SneakyThrows(Exception.class)
    public GetUserTokenResponseBody getUserAccessTokenByAuthCode(String authCode, AppKeySecretParameter parameter) {
        Client client = this.createAuthClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                .setClientId(parameter.getAppKey())
                .setClientSecret(parameter.getAppSecret())
                .setCode(authCode)
                .setGrantType(DingtalkGrantTypeEnum.AUTHORIZATION_CODE.getGrantType());
        try {
            GetUserTokenResponse userToken = client.getUserToken(getUserTokenRequest);
            return userToken.getBody();
        } catch (TeaException e) {
            throw new DingtalkApiException(e.getMessage(), e);
        }
    }

    /**
     * 通过刷新token获取用户的access token
     * @param refreshToken 刷新token
     * @param parameter 参数
     * @return token
     */
    @SneakyThrows(Exception.class)
    public GetUserTokenResponseBody getUserAccessTokenByRefreshToken(String refreshToken, AppKeySecretParameter parameter) {
        Client client = this.createAuthClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                .setClientId(parameter.getAppKey())
                .setClientSecret(parameter.getAppSecret())
                .setRefreshToken(refreshToken)
                .setGrantType(DingtalkGrantTypeEnum.REFRESH_TOKEN.getGrantType());
        try {
            GetUserTokenResponse userToken = client.getUserToken(getUserTokenRequest);
            return userToken.getBody();
        } catch (TeaException e) {
            throw new DingtalkApiException(e.getMessage(), e);
        }
    }

    private String getCacheKey(String key) {
        return KEY_PREFIX + key;
    }
}
