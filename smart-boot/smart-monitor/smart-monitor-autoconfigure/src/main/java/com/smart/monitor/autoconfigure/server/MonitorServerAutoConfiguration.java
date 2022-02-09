package com.smart.monitor.autoconfigure.server;

import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.client.ClientManagerProvider;
import com.smart.monitor.server.core.SmartMonitorServer;
import com.smart.monitor.server.core.client.ClientIdGenerator;
import com.smart.monitor.server.core.client.ClientProcessor;
import com.smart.monitor.server.core.client.HashClientIdGenerator;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.client.repository.MemoryClientRepositoryImpl;
import com.smart.monitor.server.core.context.DefaultMonitorContextImpl;
import com.smart.monitor.server.core.context.MonitorContext;
import com.smart.monitor.server.core.monitor.StatusMonitorManager;
import com.smart.monitor.server.core.task.TaskSchedulerProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMonitorServer.class)
@ComponentScan(basePackages = {
        "com.smart.monitor.server.core.controller"
})
@EnableConfigurationProperties(MonitorServerProperties.class)
public class MonitorServerAutoConfiguration {

    /**
     * monitor上下文
     * @return MonitorContext
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorContext monitorContext() {
        return new DefaultMonitorContextImpl();
    }

    /**
     * 创建ID生成器
     * @return ID生成器
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientIdGenerator clientIdGenerator() {
        return new HashClientIdGenerator();
    }

    /**
     * 创建客户端仓库
     * @param clientIdGenerator 客户端ID生成接口
     * @return 客户端仓库
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientRepository clientRepository(ClientIdGenerator clientIdGenerator , ClientManagerProvider clientManagerProvider) {
        return new MemoryClientRepositoryImpl(clientIdGenerator, clientManagerProvider);
    }

    /**
     * 状态监测定时任务提供器
     * @return TaskSchedulerProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public TaskSchedulerProvider taskSchedulerProvider() {
        return new TaskSchedulerProvider();
    }

    /**
     * 状态监控管理器
     * @param taskSchedulerProvider taskSchedulerProvider
     * @return ClientMonitorListener
     */
    @Bean
    @ConditionalOnMissingBean
    public StatusMonitorManager statusMonitorManager(TaskSchedulerProvider taskSchedulerProvider) {
        return new StatusMonitorManager(taskSchedulerProvider);
    }

    /**
     * 创建客户端处理器
     * @return ClientProcessor
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientProcessor clientProcessor() {
        return new ClientProcessor();
    }
}
