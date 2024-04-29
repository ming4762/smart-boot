package com.smart.dingtalk.api;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.smart.dingtalk.constants.url.DingTalkApiUrl;
import com.smart.dingtalk.exception.DingtalkApiException;
import com.taobao.api.TaobaoResponse;

/**
 * @author shizhongming
 * 2024/4/28 15:24
 * @since 3.0.0
 */
public abstract class AbstractDingtalkApi {

    private static final String SUCCESS_CODE = "0";

    /**
     * 获取旧版API client
     * @param apiUrl 地址
     * @return DingTalkClient
     */
    protected DingTalkClient getOldClient(DingTalkApiUrl apiUrl) {
        return new DefaultDingTalkClient(apiUrl.getUrl());
    }

    /**
     * 校验接口调用结果
     * @param response 接口调用接口
     */
    protected void validateResponse(TaobaoResponse response) {
        if (SUCCESS_CODE.equals(response.getErrorCode())) {
            return;
        }
        throw new DingtalkApiException(response);
    }
}
