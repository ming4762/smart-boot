package com.smart.monitor.server.manager.config;

import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.manager.service.MonitorClientLogService;
import com.smart.monitor.server.manager.sync.MonitorClientLogSync;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据同步配置类
 * @author ShiZhongMing
 * 2022/8/19
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
public class MonitorDataSyncConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "smart.monitor.server.dataSync.log", name = "enabled")
    public MonitorClientLogSync monitorClientLogSync(ClientWebProxy clientWebProxy, MonitorClientLogService monitorClientLogService) {
        return new MonitorClientLogSync(clientWebProxy, monitorClientLogService);
    }
}
