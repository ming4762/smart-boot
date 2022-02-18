package com.smart.monitor.autoconfigure.server;

import com.smart.commons.core.spring.EnableRest;
import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.client.ClientManagerProvider;
import com.smart.monitor.server.core.SmartMonitorServer;
import com.smart.monitor.server.core.client.ClientIdGenerator;
import com.smart.monitor.server.core.client.ClientProcessor;
import com.smart.monitor.server.core.client.HashClientIdGenerator;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.client.repository.MemoryClientRepositoryImpl;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.core.context.DefaultMonitorContextImpl;
import com.smart.monitor.server.core.context.MonitorContext;
import com.smart.monitor.server.core.event.MonitorEventPublisher;
import com.smart.monitor.server.core.event.store.DefaultMemoryMonitorEventStore;
import com.smart.monitor.server.core.event.store.MonitorEventStore;
import com.smart.monitor.server.core.event.store.MonitorEventStoreHandler;
import com.smart.monitor.server.core.monitor.StatusMonitor;
import com.smart.monitor.server.core.monitor.StatusMonitorManager;
import com.smart.monitor.server.core.monitor.status.HealthStatusMonitor;
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
@EnableRest
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

    @Bean
    @ConditionalOnMissingBean
    public ClientWebProxy clientWebProxy(ClientRepository clientRepository) {
        return new ClientWebProxy(clientRepository);
    }

    /**
     * health 状态检测
     * @param clientWebProxy ClientWebProxy
     * @param eventPublisher 事件发布器
     * @param clientRepository 客户端仓库
     * @param clientProcessor ClientProcessor
     * @return healthStatusMonitor
     */
    @Bean
    @ConditionalOnMissingBean
    public StatusMonitor healthStatusMonitor(ClientWebProxy clientWebProxy, MonitorEventPublisher eventPublisher, ClientRepository clientRepository, ClientProcessor clientProcessor) {
        return new HealthStatusMonitor(clientWebProxy, eventPublisher, clientRepository, clientProcessor);
    }

    /**
     * 创建事件存储控制器
     * @return MonitorEventStoreHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorEventStoreHandler monitorEventStoreHandler() {
        return new MonitorEventStoreHandler();
    }

    /**
     * 创建默认的事件存储器：内存事件存储器
     * @return DefaultMemoryMonitorEventStore
     */
    @Bean
    @ConditionalOnMissingBean(MonitorEventStore.class)
    public MonitorEventStore memoryMonitorEventStore() {
        return new DefaultMemoryMonitorEventStore();
    }
}
