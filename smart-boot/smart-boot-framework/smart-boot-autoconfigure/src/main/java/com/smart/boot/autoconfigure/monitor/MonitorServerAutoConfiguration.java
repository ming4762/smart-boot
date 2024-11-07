package com.smart.boot.autoconfigure.monitor;

import com.smart.framework.commons.core.spring.EnableRest;
import com.smart.framework.monitor.server.SmartMonitorServer;
import com.smart.framework.monitor.server.client.ClientIdGenerator;
import com.smart.framework.monitor.server.client.ClientProcessor;
import com.smart.framework.monitor.server.client.HashClientIdGenerator;
import com.smart.framework.monitor.server.client.repository.ClientRepository;
import com.smart.framework.monitor.server.client.repository.MemoryClientRepositoryImpl;
import com.smart.framework.monitor.server.client.request.ClientWebProxy;
import com.smart.framework.monitor.server.common.MonitorServerProperties;
import com.smart.framework.monitor.server.context.DefaultMonitorContextImpl;
import com.smart.framework.monitor.server.context.MonitorContext;
import com.smart.framework.monitor.server.event.MonitorEventPublisher;
import com.smart.framework.monitor.server.event.store.DefaultMemoryMonitorEventStore;
import com.smart.framework.monitor.server.event.store.MonitorEventStore;
import com.smart.framework.monitor.server.event.store.MonitorEventStoreHandler;
import com.smart.framework.monitor.server.monitor.StatusMonitor;
import com.smart.framework.monitor.server.monitor.StatusMonitorManager;
import com.smart.framework.monitor.server.monitor.status.HealthStatusMonitor;
import com.smart.framework.monitor.server.task.TaskSchedulerProvider;
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
        "com.smart.framework.monitor.server.controller"
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
     * @return 客户端仓库
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientRepository clientRepository() {
        return new MemoryClientRepositoryImpl();
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
