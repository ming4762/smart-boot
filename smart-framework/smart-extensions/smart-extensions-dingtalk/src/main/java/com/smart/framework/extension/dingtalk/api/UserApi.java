package com.smart.framework.extension.dingtalk.api;

import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponse;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.smart.framework.extension.dingtalk.constants.url.DingTalkUserApiUrlEnum;
import com.smart.framework.extension.dingtalk.pojo.dto.GetUserByMobileResult;
import com.smart.framework.extension.dingtalk.pojo.parameter.AppKeySecretParameter;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 钉钉用户接口
 * @author shizhongming
 * 2024/4/28 15:20
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class UserApi extends AbstractDingtalkApi {

    private final AccessSecureApi accessSecureApi;

    /**
     * 通过手机号获取用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    @SneakyThrows(ApiException.class)
    public GetUserByMobileResult getByMobile(String mobile, AppKeySecretParameter accessTokenParameter) {
        Assert.notNull(mobile, "mobile must not be null");
        OapiV2UserGetbymobileRequest request = new OapiV2UserGetbymobileRequest();
        request.setMobile(mobile);
        OapiV2UserGetbymobileResponse response = this.getOldClient(DingTalkUserApiUrlEnum.GET_BY_MOBILE).execute(request, this.accessSecureApi.getInnerAppAccessToken(accessTokenParameter).getAccessToken());
        this.validateResponse(response);
        GetUserByMobileResult result = new GetUserByMobileResult(response.getResult().getUserid());
        result.setRequestId(response.getRequestId());
        return result;
    }

    /**
     * 批量通过手机号获取用户信息
     * @param mobiles 手机号
     * @param accessTokenParameter token信息
     * @return 用户信息
     */
    public Map<String, GetUserByMobileResult> batchGetByMobile(List<String> mobiles, AppKeySecretParameter accessTokenParameter) {
        if (CollectionUtils.isEmpty(mobiles)) {
            return Map.of();
        }
        Map<String, GetUserByMobileResult> result = HashMap.newHashMap(mobiles.size());
        mobiles.forEach(mobile -> result.put(mobile, this.getByMobile(mobile, accessTokenParameter)));
        return result;
    }

    /**
     * 通过用户token获取获取用户通讯录个人信息
     * @param userAccessToken 用户token
     * @return 获取用户通讯录个人信息
     */
    @SneakyThrows(Exception.class)
    public GetUserResponseBody getUserByUserToken(String userAccessToken) {
        com.aliyun.dingtalkcontact_1_0.Client client = this.createContactClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.setXAcsDingtalkAccessToken(userAccessToken);
        GetUserResponse userResponse = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions());
        this.validateResponse(userResponse);

        return userResponse.getBody();
    }
}
