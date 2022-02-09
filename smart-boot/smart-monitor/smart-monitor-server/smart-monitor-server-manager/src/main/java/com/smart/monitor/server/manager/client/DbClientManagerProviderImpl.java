package com.smart.monitor.server.manager.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.client.ClientManagerProvider;
import com.smart.monitor.server.common.model.ClientManagerData;
import com.smart.monitor.server.manager.model.MonitorApplicationPO;
import com.smart.monitor.server.manager.service.MonitorApplicationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/3/22 9:35
 * @since 1.0
 */
@Component
public class DbClientManagerProviderImpl implements ClientManagerProvider {

    private final MonitorApplicationService monitorApplicationService;

    private final MonitorServerProperties serverProperties;

    public DbClientManagerProviderImpl(MonitorApplicationService monitorApplicationService, MonitorServerProperties serverProperties) {
        this.monitorApplicationService = monitorApplicationService;
        this.serverProperties = serverProperties;
    }

    @Override
    public ClientManagerData getByName(@NonNull String applicationName) {
        final List<MonitorApplicationPO> applicationList = this.monitorApplicationService.list(
                new QueryWrapper<MonitorApplicationPO>().lambda()
                        .eq(MonitorApplicationPO :: getApplicationCode, applicationName)
                        .eq(MonitorApplicationPO :: getUseYn, true)
        );
        if (applicationList.isEmpty()) {
            return null;
        }
        final MonitorApplicationPO application = applicationList.get(0);
        final Duration statusInterval = application.getStatusInterval() == null ? this.serverProperties.getClient().getStatusInterval() : Duration.ofMillis(application.getStatusInterval());
        final Duration offlineInterval = application.getOfflineInterval() == null ? this.serverProperties.getClient().getOfflineInterval() : Duration.ofMillis(application.getOfflineInterval());
        final ClientManagerData clientManager = ClientManagerData.builder()
                .id(application.getId())
                .applicationName(applicationName)
                .name(application.getName())
                .remark(application.getRemark())
                .statusInterval(statusInterval)
                .offlineInterval(offlineInterval)
                .token(application.getToken())
                .build();
        // 设置事件编码
        final String eventCodes = application.getSerializeEventCode();
        if (StringUtils.isNotBlank(eventCodes)) {
            clientManager.setSerializeEventCodes(
                    Arrays.stream(eventCodes.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet())
            );
        }
        return clientManager;
    }
}
