package com.smart.monitor.server.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import com.smart.monitor.server.manager.service.MonitorClientService;
import com.smart.monitor.server.manager.service.MonitorUserGroupApplicationService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/2/10
 * @since 2.0.0
 */
@Service
public class MonitorClientServiceImpl implements MonitorClientService {

    private final ClientRepository clientRepository;

    private final MonitorUserGroupApplicationService monitorUserGroupApplicationService;

    private final MonitorApplicationService monitorApplicationService;

    public MonitorClientServiceImpl(ClientRepository clientRepository, MonitorUserGroupApplicationService monitorUserGroupApplicationService, MonitorApplicationService monitorApplicationService) {
        this.clientRepository = clientRepository;
        this.monitorUserGroupApplicationService = monitorUserGroupApplicationService;
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
        List<Long> applicationIdList = this.monitorUserGroupApplicationService.listApplicationIdByUserId(userId);
        if (CollectionUtils.isEmpty(applicationIdList)) {
            return new ArrayList<>(0);
        }
        // 查询客户端名称
        List<String> appNameList = this.monitorApplicationService.list(
                new QueryWrapper<MonitorApplicationPO>().lambda()
                .select(MonitorApplicationPO :: getApplicationCode)
                .eq(MonitorApplicationPO :: getUseYn, true)
                .in(MonitorApplicationPO :: getId, applicationIdList)
        ).stream().map(MonitorApplicationPO :: getApplicationCode).collect(Collectors.toList());
        return new ArrayList<>(this.clientRepository.findByName(appNameList, active));
    }
}
