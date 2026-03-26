package com.smart.framework.n8n.api;

import com.smart.framework.commons.core.utils.Base64Utils;
import com.smart.framework.commons.core.utils.RestUtils;
import com.smart.framework.n8n.constants.SmartN8nAuthTypeEnum;
import com.smart.framework.n8n.properties.SmartN8nAuthBasicProperties;
import com.smart.framework.n8n.properties.SmartN8nAuthHeaderProperties;
import com.smart.framework.n8n.properties.SmartN8nAuthProperties;
import com.smart.framework.n8n.properties.SmartN8nProperties;
import com.smart.framework.n8n.service.SmartN8nService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.jspecify.annotations.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * N8n Webhook API
 * java调用N8n Webhook API
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 19:51
 * @since 5.0.0
 */
public class SmartN8nWebhookApi {

    private static final String URL_SUFFIX = "/webhook";

    private final String id;
    private final SmartN8nService n8nService;

    private SmartN8nAuthTypeEnum authType;

    private String basicAuthUsername;
    private String basicAuthPassword;

    private String headerAuthName;
    private String headerAuthValue;

    public SmartN8nWebhookApi(String id, SmartN8nService n8nService) {
        this.id = id;
        this.n8nService = n8nService;
        // 默认无认证
        this.authType = SmartN8nAuthTypeEnum.NONE;
    }

    /**
     * 开启Basic Auth认证
     * 使用默认的Basic Auth认证配置
     * @return this
     */
    public SmartN8nWebhookApi basicAuth() {
        SmartN8nAuthBasicProperties basicProperties = Optional.of(this.n8nService.getProperties())
                .map(SmartN8nProperties::getAuth)
                .map(SmartN8nAuthProperties::getBasic)
                .orElseThrow();
        return this.basicAuth(basicProperties.getUser(), basicProperties.getPassword());
    }

    /**
     * 开启Basic Auth认证
     * @param username Basic Auth用户名
     * @param password Basic Auth密码
     * @return this
     */
    public SmartN8nWebhookApi basicAuth(@NonNull String username, @NonNull String password) {
        this.authType = SmartN8nAuthTypeEnum.BASIC;
        this.basicAuthUsername = username;
        this.basicAuthPassword = password;
        return this;
    }

    /**
     * 开启Header Auth认证
     * 使用默认的Header Auth认证配置
     * @return this
     */
    public SmartN8nWebhookApi headerAuth() {
        SmartN8nAuthHeaderProperties headerProperties = Optional.of(this.n8nService.getProperties())
                .map(SmartN8nProperties::getAuth)
                .map(SmartN8nAuthProperties::getHeader)
                .orElseThrow();
        return this.headerAuth(headerProperties.getHeaderName(), headerProperties.getHeaderValue());
    }

    /**
     * 开启Header Auth认证
     * @param headerName Header Auth名称
     * @param headerValue Header Auth值
     * @return this
     */
    public SmartN8nWebhookApi headerAuth(@NonNull String headerName, @NonNull String headerValue) {
        this.authType = SmartN8nAuthTypeEnum.HEADER;
        this.headerAuthName = headerName;
        this.headerAuthValue = headerValue;
        return this;
    }

    /**
     * 执行N8n Webhook API
     * @return 调用结果
     * @param <T> 调用结果类型
     */
    public <T> T call(Object payload, ParameterizedTypeReference<T> responseType) {
        String url = this.getUrl();
        Map<String, String> headers = this.getHeaders();
        return RestUtils.rest(
                url,
                HttpMethod.POST,
                headers,
                payload,
                responseType,
                null
        );
    }

    /**
     * 获取N8n Webhook API调用所需的HTTP头
     * @return HTTP头
     */
    private Map<String, String> getHeaders() {
        HashMap<String, String> headers = HashMap.newHashMap(10);
        if (SmartN8nAuthTypeEnum.HEADER.equals(this.authType)) {
            headers.put(this.headerAuthName, this.headerAuthValue);
        } else if (SmartN8nAuthTypeEnum.BASIC.equals(this.authType)) {
            headers.put("Authorization", "Basic " + Base64Utils.encode(this.basicAuthUsername + ":" + this.basicAuthPassword));
        }
        return headers;
    }


    protected String getUrl() {
        return UriComponentsBuilder.fromPath(this.n8nService.getProperties().getBaseUrl())
                .path(URL_SUFFIX)
                .path(this.id)
                .toUriString();
    }
}
