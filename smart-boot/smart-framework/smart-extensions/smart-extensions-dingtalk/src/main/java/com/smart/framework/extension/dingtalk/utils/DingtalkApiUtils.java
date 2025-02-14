package com.smart.framework.extension.dingtalk.utils;

import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.framework.extension.dingtalk.constants.url.DingTalkApiUrl;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

/**
 * dingding api URL
 * @author shizhongming
 * 2024/4/29 9:20
 * @since 3.0.0
 */
public class DingtalkApiUtils {

    private static final String URL_FORMATTER = "%s?access_token=%s";

    private DingtalkApiUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public <T> T send(DingTalkApiUrl apiUrl, String accessToken, Serializable parameter) {
        String url = String.format(URL_FORMATTER, apiUrl.getUrl(), accessToken);
        return RestUtils.rest(url, HttpMethod.POST, Map.of(), parameter, new ParameterizedTypeReference<>() {
        }, null);
    }
}
