package com.smart.monitor.server.core.controller;

import com.smart.commons.core.message.Result;
import com.smart.monitor.core.constants.CommonUrlConstants;
import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.core.client.ClientProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端注册接口
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@RestController
@RequestMapping
@Slf4j
public class ClientRegisterController {

    private final ClientProcessor clientProcessor;

    public ClientRegisterController(ClientProcessor clientProcessor) {
        this.clientProcessor = clientProcessor;
    }

    /**
     * 客户端注册接口
     * @param application 应用信息
     * @return 客户端ID
     */
    @PostMapping(CommonUrlConstants.REGISTER_URL)
    public Result<String> register(@RequestBody Application application) {
        try {
            ClientData clientData = this.clientProcessor.register(application);
            if (clientData == null) {
                return Result.failure(String.format("client is not manager，please manager it，application name: %s", application.getApplicationName()));
            }
            return Result.success(clientData.getId().getValue());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 客户端注销接口
     * @param id 客户端ID
     * @return 注销是否成功
     */
    @PostMapping(CommonUrlConstants.DEREGISTER_URL)
    public Result<Boolean> deregister(@RequestBody String id) {
        ClientData clientData = this.clientProcessor.deregister(ClientId.create(id));
        if (clientData == null) {
            log.warn("deregister fail, id not found, id: {}", id);
            return Result.success(false);
        }
        log.info("deregister success, id {}", id);
        return Result.success(true);
    }
}
