package com.smart.dingtalk.api;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.smart.commons.core.cache.CacheService;
import com.smart.dingtalk.pojo.dto.GetAccessTokenResult;
import com.smart.dingtalk.pojo.parameter.GetAccessTokenParameter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 认证服务类
 * @author shizhongming
 * 2024/4/26 21:32
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class AccessSecureApi {

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
    public GetAccessTokenResult getInnerAppAccessToken(GetAccessTokenParameter parameter) {
        GetAccessTokenResult accessTokenResult = this.cacheService.get(this.getCacheKey(parameter.getAppKey()));
        if (accessTokenResult != null) {
            return accessTokenResult;
        }
        // 请求获取access token
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        Client client = new Client(config);

        GetAccessTokenRequest accessTokenRequest = new GetAccessTokenRequest()
                .setAppKey(parameter.getAppKey())
                .setAppSecret(parameter.getAppSecret());

        GetAccessTokenResponse response = client.getAccessToken(accessTokenRequest);
        GetAccessTokenResponseBody body = response.getBody();

        // 计算有效期
        Duration duration = Duration.ofSeconds(body.getExpireIn()).minus(ACCESS_TOKEN_EXPIRES_DURATION);
        LocalDateTime expireAt = LocalDateTime.now().plus(duration);

        GetAccessTokenResult result = new GetAccessTokenResult(body.accessToken, expireAt);
        // 设置缓存
        this.cacheService.put(this.getCacheKey(parameter.getAppKey()), result, duration);

        return result;
    }


    protected String getCacheKey(String key) {
        return KEY_PREFIX + key;
    }
}
