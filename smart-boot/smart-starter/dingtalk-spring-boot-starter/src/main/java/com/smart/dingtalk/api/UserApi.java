package com.smart.dingtalk.api;

import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.smart.dingtalk.constants.DingtalkApiUrlEnum;
import com.smart.dingtalk.pojo.dto.GetUserByMobileResult;
import com.smart.dingtalk.pojo.parameter.GetAccessTokenParameter;
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
    public GetUserByMobileResult getByMobile(String mobile, GetAccessTokenParameter accessTokenParameter) {
        Assert.notNull(mobile, "mobile must not be null");
        OapiV2UserGetbymobileRequest request = new OapiV2UserGetbymobileRequest();
        request.setMobile(mobile);
        OapiV2UserGetbymobileResponse response = this.getOldClient(DingtalkApiUrlEnum.GET_USER_BY_MOBILE).execute(request, this.accessSecureApi.getInnerAppAccessToken(accessTokenParameter).getAccessToken());
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
    public Map<String, GetUserByMobileResult> batchGetByMobile(List<String> mobiles, GetAccessTokenParameter accessTokenParameter) {
        if (CollectionUtils.isEmpty(mobiles)) {
            return Map.of();
        }
        Map<String, GetUserByMobileResult> result = new HashMap<>(mobiles.size());
        mobiles.forEach(mobile -> result.put(mobile, this.getByMobile(mobile, accessTokenParameter)));
        return result;
    }
}
