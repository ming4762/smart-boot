package com.smart.monitor.autoconfigure.server;

import com.smart.monitor.server.core.SmartMonitorServer;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.sync.DataSyncDispatcher;
import com.smart.monitor.server.core.task.TaskSchedulerProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ShiZhongMing
 * 2022/2/22
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "smart.monitor.server.data-sync", name = "enabled", matchIfMissing = true)
@ConditionalOnClass(SmartMonitorServer.class)
@AutoConfigureAfter(MonitorServerAutoConfiguration.class)
public class MonitorDataSyncAutoConfiguration {

    @Bean
    public DataSyncDispatcher dataSyncDispatcher(TaskSchedulerProvider taskSchedulerProvider, ClientRepository clientRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
       return new DataSyncDispatcher(taskSchedulerProvider, clientRepository, threadPoolTaskExecutor);
    }
}
