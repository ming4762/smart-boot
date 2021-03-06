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
     * monitor?????????
     * @return MonitorContext
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorContext monitorContext() {
        return new DefaultMonitorContextImpl();
    }

    /**
     * ??????ID?????????
     * @return ID?????????
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientIdGenerator clientIdGenerator() {
        return new HashClientIdGenerator();
    }

    /**
     * ?????????????????????
     * @param clientIdGenerator ?????????ID????????????
     * @return ???????????????
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientRepository clientRepository(ClientIdGenerator clientIdGenerator , ClientManagerProvider clientManagerProvider) {
        return new MemoryClientRepositoryImpl(clientIdGenerator, clientManagerProvider);
    }

    /**
     * ?????????????????????????????????
     * @return TaskSchedulerProvider
     */
    @Bean
    @ConditionalOnMissingBean
    public TaskSchedulerProvider taskSchedulerProvider() {
        return new TaskSchedulerProvider();
    }

    /**
     * ?????????????????????
     * @param taskSchedulerProvider taskSchedulerProvider
     * @return ClientMonitorListener
     */
    @Bean
    @ConditionalOnMissingBean
    public StatusMonitorManager statusMonitorManager(TaskSchedulerProvider taskSchedulerProvider) {
        return new StatusMonitorManager(taskSchedulerProvider);
    }

    /**
     * ????????????????????????
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
     * health ????????????
     * @param clientWebProxy ClientWebProxy
     * @param eventPublisher ???????????????
     * @param clientRepository ???????????????
     * @param clientProcessor ClientProcessor
     * @return healthStatusMonitor
     */
    @Bean
    @ConditionalOnMissingBean
    public StatusMonitor healthStatusMonitor(ClientWebProxy clientWebProxy, MonitorEventPublisher eventPublisher, ClientRepository clientRepository, ClientProcessor clientProcessor) {
        return new HealthStatusMonitor(clientWebProxy, eventPublisher, clientRepository, clientProcessor);
    }

    /**
     * ???????????????????????????
     * @return MonitorEventStoreHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public MonitorEventStoreHandler monitorEventStoreHandler() {
        return new MonitorEventStoreHandler();
    }

    /**
     * ??????????????????????????????????????????????????????
     * @return DefaultMemoryMonitorEventStore
     */
    @Bean
    @ConditionalOnMissingBean(MonitorEventStore.class)
    public MonitorEventStore memoryMonitorEventStore() {
        return new DefaultMemoryMonitorEventStore();
    }
}
