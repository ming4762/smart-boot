package com.smart.dingtalk.utils;

import com.smart.commons.core.utils.RestUtils;
import com.smart.dingtalk.constants.url.DingTalkApiUrl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

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

    public <T> T send(DingTalkApiUrl apiUrl, String accessToken, Serializable parameter, Class<T> resultType) {
        String url = String.format(URL_FORMATTER, apiUrl.getUrl(), accessToken);
        ResponseEntity<T> response = RestUtils.rest(url, HttpMethod.POST, Map.of(), parameter, resultType);
        return response.getBody();
    }
}
