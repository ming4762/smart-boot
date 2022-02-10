package com.smart.monitor.server.core.client.request;

import com.google.common.collect.Maps;
import com.smart.commons.core.exception.IORuntimeException;
import com.smart.commons.core.message.Result;
import com.smart.commons.core.utils.JsonUtils;
import com.smart.commons.core.utils.RestUtils;
import com.smart.monitor.core.constants.CommonHeadersEnum;
import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.controller.ClientProxyController;
import com.smart.monitor.server.core.exception.ClientDownException;
import com.smart.monitor.server.core.exception.ClientNoRegisterException;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 客户端web接口代理
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
public class ClientWebProxy {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final ClientRepository clientRepository;

    public ClientWebProxy(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void forward(@NonNull ClientId clientId, HttpServletRequest request, HttpServletResponse response, boolean needUp) {
        this.forward(clientId, (repositoryData) -> this.createForwardRequest(request, repositoryData), (result) -> {
            if (result != null) {
                try {
                    response.getWriter().write(JsonUtils.toJsonString(Result.success(result)));
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        }, needUp);
    }

    /**
     * forward
     * @param clientId 客户端ID
     * @param requestHandler 请求回调
     * @param resultHandler 结果回调
     * @param needUp 客户端是否必须在线，true则客户端不在线抛出异常
     */
    public <T> void forward(@NonNull ClientId clientId, Function<ClientData, ForwardRequest> requestHandler, Consumer<T> resultHandler, boolean needUp, Class<T> resultClass) {
        // 获取客户端信息
        final ClientData repositoryData = this.clientRepository.findById(clientId, false);
        if (repositoryData == null) {
            throw new ClientNoRegisterException(clientId);
        }
        this.forward(repositoryData, requestHandler, resultHandler, needUp, resultClass);
    }

    /**
     * forward
     * @param clientId 客户端ID
     * @param requestHandler 请求回调
     * @param resultHandler 结果回调
     * @param needUp 客户端是否必须在线，true则客户端不在线抛出异常
     */
    public void forward(@NonNull ClientId clientId, Function<ClientData, ForwardRequest> requestHandler, Consumer<Object> resultHandler, boolean needUp) {
        this.forward(clientId, requestHandler, resultHandler, needUp, Object.class);
    }

    /**
     * forward
     * @param clientData 客户端数据
     * @param requestHandler 请求回调
     * @param resultHandler 结果回调
     * @param needUp 客户端是否必须在线，true则客户端不在线抛出异常
     */
    public <T> void forward(@NonNull ClientData clientData, Function<ClientData, ForwardRequest> requestHandler, Consumer<T> resultHandler, boolean needUp, Class<T> resultClass) {
        // 获取客户端信息
        if (needUp && clientData.getStatus().equals(ClientStatusEnum.DOWN)) {
            throw new ClientDownException(clientData.getId());
        }
        ForwardRequest request = requestHandler.apply(clientData);
        // 添加token
        this.addTokenHeader(request, clientData);
        // 发送请求
        T result = RestUtils.rest(
                this.getUrl(clientData, request.getUri()),
                request.getHttpMethod(),
                request.getHttpHeaders(),
                request.getBody(),
                resultClass
        ).getBody();
        resultHandler.accept(result);
    }

    protected String getUrl(@NonNull ClientData repositoryData, @NonNull String url) {
        return repositoryData.getApplication().getClientUrl() + url;
    }

    /**
     * 创建跳转请求信息
     * @param request HttpServletRequest
     * @param clientData 客户端数据
     * @return 跳转请求信息
     */
    @SneakyThrows
    public ForwardRequest createForwardRequest(@NonNull HttpServletRequest request, @NonNull ClientData clientData) {
        //  TODO:开发中
        String url = "/actuator/" + UriComponentsBuilder.fromPath(this.pathMatcher.extractPathWithinPattern(ClientProxyController.CLIENT_PROXY_PATH, request.getRequestURI())).build(true).toUri();
        // 添加参数
        Enumeration<String> parameterNames = request.getParameterNames();
        final List<String> parameters = new ArrayList<>();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            parameters.add(name + "=" + request.getParameter(name));
        }
        if (!parameters.isEmpty()) {
            url += "?" + String.join("&", parameters.toArray(new String[]{}));
        }
        return ForwardRequest.builder()
                .uri(url)
                .httpMethod(HttpMethod.valueOf(request.getMethod()))
                .httpHeaders(this.createHttpHeaders(request, clientData))
                .body(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8))
                .build();
    }

    /**
     * 创建请求头信息
     * @param request HttpServletRequest
     * @return 请求头
     */
    protected Map<String, String> createHttpHeaders(@NonNull HttpServletRequest request, @NonNull ClientData clientData) {
        final Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.CONTENT_TYPE));
        return headerMap;
    }

    /**
     * 添加token请求头
     * @param request 请求信息
     * @param clientData 客户端信息
     * @return 请求信息
     */
    protected ForwardRequest addTokenHeader(ForwardRequest request, @NonNull ClientData clientData) {
        // 设置认证请求头
        final String token = clientData.getToken();
        if (StringUtils.hasLength(token)) {
            request.getHttpHeaders().put(CommonHeadersEnum.Monitor_Authorization.name(), token);
        }
        return request;
    }

    /**
     * 请求头信息
     */
    @Builder
    @Getter
    public static class ForwardRequest {
        private final String uri;

        private final HttpMethod httpMethod;

        @Builder.Default
        private final Map<String, String> httpHeaders = new HashMap<>();

        private final Object body;
    }
}
