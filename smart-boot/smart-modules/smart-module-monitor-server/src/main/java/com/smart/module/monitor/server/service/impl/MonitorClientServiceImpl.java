package com.smart.module.monitor.server.service.impl;

import com.smart.framework.monitor.server.client.repository.ClientRepository;
import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.module.monitor.server.service.MonitorApplicationService;
import com.smart.module.monitor.server.service.MonitorClientService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/2/10
 * @since 2.0.0
 */
@Service
public class MonitorClientServiceImpl implements MonitorClientService {

    private final ClientRepository clientRepository;

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientServiceImpl(ClientRepository clientRepository, MonitorApplicationService monitorApplicationService) {
        this.clientRepository = clientRepository;
        this.monitorApplicationService = monitorApplicationService;
    }


    /**
     * 查询注册客户端列表
     * @param active 是否只查询激活的
     * @return 客户端列表
     */
    @NonNull
    @Override
    public List<ClientData> listRegisterClient(boolean active) {
        return new ArrayList<>(this.clientRepository.findAll(active));
    }

    /**
     * 查询用户关联客户端列表
     * @param userId 用户ID
     * @param active 是否激活
     * @return 客户端列表
     */
    @NonNull
    @Override
    public List<ClientData> listUserClient(@NonNull Long userId, boolean active) {
        return new ArrayList<>(this.clientRepository.findByName(this.monitorApplicationService.listApplicationNameByUser(userId), active));
    }
}
