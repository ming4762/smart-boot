package com.smart.framework.extension.dingtalk.api;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClient;
import com.smart.framework.extension.dingtalk.client.SmartDingtalkClientHolder;
import com.smart.framework.extension.dingtalk.constants.DingtalkClientTypeEnum;
import com.smart.framework.extension.dingtalk.constants.DingtalkGrantTypeEnum;
import com.smart.framework.extension.dingtalk.exception.DingtalkApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 认证服务类
 * @author shizhongming
 * 2024/4/26 21:32
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class AccessSecureApi extends AbstractDingtalkApi {

    private final SmartDingtalkClientHolder smartDingtalkClientHolder;

    /**
     * 获取应用内部access token
     * @return token
     */
    @SneakyThrows(Exception.class)
    public String getInnerAppAccessToken() {
        SmartDingtalkClient smartDingtalkClient = this.smartDingtalkClientHolder.getClient();
        if (!DingtalkClientTypeEnum.INNER.equals(smartDingtalkClient.getClientType())) {
            throw new DingtalkApiException("当前正在使用的钉钉应用[{}]不是企业内部应用", smartDingtalkClient.getClientId());
        }
        if (!smartDingtalkClient.isAccessTokenExpired() && smartDingtalkClient.getAccessToken() != null) {
            return smartDingtalkClient.getAccessToken();
        }

        Lock lock = smartDingtalkClient.getAccessTokenLock();
        boolean locked = false;
        try {
            do {
                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!smartDingtalkClient.isAccessTokenExpired() && smartDingtalkClient.getAccessToken() != null) {
                    return smartDingtalkClient.getAccessToken();
                }
            } while (!locked);
            // 执行API调用
            Client client = this.createAuthClient();
            // 请求获取access token
            GetAccessTokenRequest accessTokenRequest = new GetAccessTokenRequest()
                    .setAppKey(smartDingtalkClient.getClientId())
                    .setAppSecret(smartDingtalkClient.getClientSecret());

            GetAccessTokenResponse response = client.getAccessToken(accessTokenRequest);
            GetAccessTokenResponseBody body = response.getBody();
            // 更新应用token
            smartDingtalkClient.updateAccessToken(body.accessToken, body.expireIn);
            return body.accessToken;
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    /**
     * 通过授权码获取用户的access token
     * @param authCode 授权码
     * @return token
     */
    @SneakyThrows(Exception.class)
    public GetUserTokenResponseBody getUserAccessTokenByAuthCode(String authCode) {
        SmartDingtalkClient smartDingtalkClient = this.smartDingtalkClientHolder.getClient();

        Client client = this.createAuthClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                .setClientId(smartDingtalkClient.getClientId())
                .setClientSecret(smartDingtalkClient.getClientSecret())
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
     * @return token
     */
    @SneakyThrows(Exception.class)
    public GetUserTokenResponseBody getUserAccessTokenByRefreshToken(String refreshToken) {
        SmartDingtalkClient smartDingtalkClient = this.smartDingtalkClientHolder.getClient();
        Client client = this.createAuthClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
                .setClientId(smartDingtalkClient.getClientId())
                .setClientSecret(smartDingtalkClient.getClientSecret())
                .setRefreshToken(refreshToken)
                .setGrantType(DingtalkGrantTypeEnum.REFRESH_TOKEN.getGrantType());
        try {
            GetUserTokenResponse userToken = client.getUserToken(getUserTokenRequest);
            return userToken.getBody();
        } catch (TeaException e) {
            throw new DingtalkApiException(e.getMessage(), e);
        }
    }
}
