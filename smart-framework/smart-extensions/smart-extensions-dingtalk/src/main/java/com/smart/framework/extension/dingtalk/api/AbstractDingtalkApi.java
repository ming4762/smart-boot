package com.smart.framework.extension.dingtalk.api;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.tea.TeaModel;
import com.aliyun.teaopenapi.models.Config;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.smart.framework.extension.dingtalk.constants.url.DingTalkApiUrl;
import com.smart.framework.extension.dingtalk.exception.DingtalkApiException;
import com.taobao.api.TaobaoResponse;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * @author shizhongming
 * 2024/4/28 15:24
 * @since 3.0.0
 */
public abstract class AbstractDingtalkApi {

    private static final String SUCCESS_CODE = "0";
    private static final String STATUS_CODE = "statusCode";

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
        throw new DingtalkApiException(response.getMessage(), response);
    }

    /**
     * 校验接口调用结果
     * @param teaModel teaModel
     */
    protected void validateResponse(TeaModel teaModel) {
        Map<String, Object> map = teaModel.toMap();
        if (SUCCESS_CODE.equals(map.get(STATUS_CODE))) {
            return;
        }
        throw new DingtalkApiException("", teaModel);
    }

    private Config createConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return config;
    }

    /**
     * 创建客户端
     * @return 客户端
     */
    @SneakyThrows(Exception.class)
    protected Client createAuthClient() {
        return new Client(this.createConfig());
    }

    @SneakyThrows(Exception.class)
    protected com.aliyun.dingtalkcontact_1_0.Client createContactClient() {
        return new com.aliyun.dingtalkcontact_1_0.Client(this.createConfig());
    }
}
