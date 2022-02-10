package com.smart.monitor.server.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.commons.core.message.Result;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

/**
 * 客户端接口
 * @author ShiZhongMing
 * 2022/2/10
 * @since 2.0.0
 */
@RestController
@RequestMapping("monitor/manager/client")
@Api("客户端管理接口")
public class MonitorClientController {

    private final MonitorClientService monitorClientService;

    private final ClientRepository clientRepository;

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientController(MonitorClientService monitorClientService, ClientRepository clientRepository, MonitorApplicationService monitorApplicationService) {
        this.monitorClientService = monitorClientService;
        this.clientRepository = clientRepository;
        this.monitorApplicationService = monitorApplicationService;
    }

    /**
     * 查询注册的客户端
     * @param active 是否是激活的
     * @return 注册的客户端
     */
    @PostMapping("listClient")
    @ApiOperation(value = "查询注册客户端列表", httpMethod = "POST")
    public Result<Collection<ClientData>> listRegisterClient(@ApiParam(name = "是否只查询激活状态", required = true) @RequestBody Boolean active) {
        boolean isActive = Objects.equals(Boolean.TRUE, active);
        return Result.success(this.monitorClientService.listRegisterClient(isActive));
    }

    /**
     * 查询当前用户有权限的注册的客户端
     * @param active 是否是激活的
     * @return 注册的客户端
     */
    @PostMapping("listUserClient")
    @ApiOperation(value = "查询当前用户有权限的注册的客户端", httpMethod = "POST")
    public Result<Collection<ClientData>> listUserClient(@ApiParam(name = "是否只查询激活状态", required = true) @RequestBody Boolean active) {
        boolean isActive = Objects.equals(Boolean.TRUE, active);
        return Result.success(this.monitorClientService.listUserClient(AuthUtils.getNonNullCurrentUserId(), isActive));
    }

    /**
     * 通过客户端ID查询
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    @PostMapping("getApplicationByClientId")
    @ApiOperation(value = "通过客户端ID查询应用信息", httpMethod = "POST")
    public Result<MonitorApplicationPO> getApplicationByClientId(@RequestBody String clientId) {
        ClientData clientData = this.clientRepository.findById(ClientId.create(clientId), false);
        if (clientData == null) {
            return Result.success();
        }
        return Result.success(
                this.monitorApplicationService.getOne(
                        new QueryWrapper<MonitorApplicationPO>().lambda()
                        .eq(MonitorApplicationPO :: getApplicationCode, clientData.getApplication().getApplicationName())
                )
        );
    }


    /**
     * 通过ID查询注册客户端信息
     * @param active 释放激活
     * @param id 客户端ID
     * @return 客户端信息
     */
    @PostMapping("getClientById/{id}")
    @ApiOperation(value = "通过ID查询注册客户端信息", httpMethod = "POST")
    public Result<ClientData> getClientById(@ApiParam(name = "是否只查询激活状态", required = true) @RequestBody Boolean active, @PathVariable("id") String id) {
        boolean isActive = Objects.equals(Boolean.TRUE, active);
        return Result.success(this.clientRepository.findById(ClientId.create(id), isActive));
    }
}
