package com.smart.monitor.client.core.registration;

import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.RestUtils;
import com.smart.monitor.client.core.exception.RegistrarException;
import com.smart.monitor.core.constants.CommonUrlConstants;
import com.smart.monitor.core.model.Application;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
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
        final Result<?> result = RestUtils.rest(url, HttpMethod.POST, this.getHeaders(), application, Result.class).getBody();

        Assert.notNull(result, "register failed， result is null");
        if (!result.isSuccess()) {
            throw new RegistrarException(result);
        }
        return (String) result.getData();
    }

    @Override
    public void deregister(String serverUrl, String applicationId) {
        RestUtils.rest(
                serverUrl + CommonUrlConstants.DEREGISTER_URL,
                HttpMethod.POST,
                this.getHeaders(),
                applicationId,
                Object.class
        );
    }

    /**
     * 获取请求头
     * @return 请求头信息
     */
    private Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>(0);
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }
}
