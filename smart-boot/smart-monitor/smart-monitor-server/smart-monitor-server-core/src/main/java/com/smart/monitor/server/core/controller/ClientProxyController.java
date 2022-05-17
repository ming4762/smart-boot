package com.smart.monitor.server.core.controller;

import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author ShiZhongMing
 * 2021/3/25 17:26
 * @since 1.0
 */
@Controller
@RequestMapping
public class ClientProxyController {

    public static final String CLIENT_PROXY_PATH = "/monitor/client/{clientId}/actuator/**";
    public static final String CLIENT_PROXY_PATH_DOWNLOAD = "/monitor/clientDownload/{clientId}/actuator/**";


    private final ClientWebProxy clientWebProxy;

    public ClientProxyController(ClientWebProxy clientWebProxy) {
        this.clientWebProxy = clientWebProxy;
    }

    /**
     * 客户端代理接口
     * @param clientId 客户端ID
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(path = CLIENT_PROXY_PATH, method = { RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS })
    public void clientProxy(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        this.clientWebProxy.forward(ClientId.create(clientId), request, response, true);
    }


    /**
     * 客户端代理接口
     * @param clientId 客户端ID
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(path = CLIENT_PROXY_PATH_DOWNLOAD, method = { RequestMethod.GET, RequestMethod.HEAD, RequestMethod.POST,
            RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS })
    public void clientProxyDownload(@PathVariable("clientId") String clientId, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        this.clientWebProxy.forwardDownload(ClientId.create(clientId), request, response, true);
    }
}
