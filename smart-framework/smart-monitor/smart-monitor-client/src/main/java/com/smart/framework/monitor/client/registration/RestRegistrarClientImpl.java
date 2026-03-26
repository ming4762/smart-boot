package com.smart.framework.monitor.client.registration;

import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.framework.monitor.client.exception.RegistrarException;
import com.smart.framework.monitor.core.constants.CommonUrlConstants;
import com.smart.framework.monitor.core.model.Application;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.jspecify.annotations.NonNull;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shizhongming
 * 2021/3/21 8:37 上午
 */
public class RestRegistrarClientImpl implements RegistrarClient {
    @NonNull
    @Override
    public String register(String serverUrl, Application application) {
        final String url = serverUrl + CommonUrlConstants.REGISTER_URL;
        final Result<String> result = RestUtils.rest(url, HttpMethod.POST, this.getHeaders(), application, new ParameterizedTypeReference<Result<String>>() {
        }, null);

        Assert.notNull(result, "register failed， result is null");
        if (!result.isSuccess()) {
            throw new RegistrarException(result);
        }
        return result.getData();
    }

    @Override
    public void deregister(String serverUrl, String applicationId) {
        RestUtils.rest(
                serverUrl + CommonUrlConstants.DEREGISTER_URL,
                HttpMethod.POST,
                this.getHeaders(),
                applicationId,
                new ParameterizedTypeReference<>() {
                },
                null
        );
    }

    /**
     * 获取请求头
     * @return 请求头信息
     */
    private Map<String, String> getHeaders() {
        final Map<String, String> headers = HashMap.newHashMap(1);
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }
}
