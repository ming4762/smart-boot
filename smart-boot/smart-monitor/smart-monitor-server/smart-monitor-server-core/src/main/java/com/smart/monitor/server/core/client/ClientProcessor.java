package com.smart.monitor.server.core.client;

import com.smart.monitor.core.model.Application;
import com.smart.monitor.server.common.MonitorServerProperties;
import com.smart.monitor.server.common.client.ClientManagerProvider;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.common.model.ClientManagerData;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.context.MonitorContext;
import com.smart.monitor.server.core.event.domain.ClientDeregisterEvent;
import com.smart.monitor.server.core.event.domain.ClientRegisteredEvent;
import com.smart.monitor.server.core.event.domain.ClientRegisteredUpdateEvent;
import com.smart.monitor.server.core.monitor.StatusMonitorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

/**
 * 客户端处理器
 * @author ShiZhongMing
 * 2022/2/8
 * @since 2.0.0
 */
@Slf4j
public class ClientProcessor {

    private ClientIdGenerator clientIdGenerator;

    private ClientRepository clientRepository;

    private ClientManagerProvider clientManagerProvider;

    /**
     * 状态监控管理器
     */
    private StatusMonitorManager statusMonitorManager;

    private MonitorContext monitorContext;

    private MonitorServerProperties serverProperties;


    /**
     * 客户端注册
     * @param application 客户端信息
     * @return 注册后客户端信息
     */
    public ClientData register(@NonNull Application application) {
        // 生成ID
        ClientId clientId = this.clientIdGenerator.create(application);
        return this.clientRepository.compute(clientId, (id, repositoryData) -> {
            // 获取客户端信息
            ClientManagerData managerData = this.clientManagerProvider.getByName(application.getApplicationName());
            if (managerData == null) {
                log.warn("client is not manager，application name: {}", application.getApplicationName());
                if (repositoryData != null) {
                    // 之前注册过，后续在客户端管理将其移除
                    // 此处进行取消注册
                    this.deregister(clientId);
                }
                return null;
            }
            if (repositoryData == null) {
                // 第一次注册
                repositoryData = new ClientData(application, clientId, managerData);
                // 如果token为null，设置默认token
                if (repositoryData.getToken() == null) {
                    repositoryData.setToken(this.serverProperties.getClient().getDefaultToken());
                }
                // 发布注册事件
                this.monitorContext.publishEvent(new ClientRegisteredEvent(repositoryData, this));
                // 启动状态监听事件
                this.statusMonitorManager.addScheduled(repositoryData);
                return repositoryData;
            }
            // 不是第一次注册
            // 刷新数据
            repositoryData.refresh(application, managerData);
            // 发布更新事件
            this.monitorContext.publishEvent(new ClientRegisteredUpdateEvent(repositoryData, this));
            this.statusMonitorManager.addScheduled(repositoryData);
            return repositoryData;
        });
    }

    /**
     * 客户端注销
     * @param clientId ID
     * @return 注销的客户端
     */
    public ClientData deregister(@NonNull ClientId clientId) {
       ClientData repositoryData = this.removeClient(clientId);
       if (repositoryData != null) {
           // 发布注销事件
           this.monitorContext.publishEvent(new ClientDeregisterEvent(repositoryData, this));
       }
       return repositoryData;
    }


    /**
     * 移除客户端
     * @param clientId 客户端ID
     * @return 客户端信息
     */
    public ClientData removeClient(@NonNull ClientId clientId) {
        // 从仓库移除
        final ClientData repositoryData = this.clientRepository.remove(clientId);
        // 移除状态监听定时任务
        this.statusMonitorManager.removeScheduled(clientId);
        return repositoryData;
    }

    @Autowired
    public void setClientIdGenerator(ClientIdGenerator clientIdGenerator) {
        this.clientIdGenerator = clientIdGenerator;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Autowired
    public void setClientManagerProvider(ClientManagerProvider clientManagerProvider) {
        this.clientManagerProvider = clientManagerProvider;
    }

    @Autowired
    public void setStatusMonitorManager(StatusMonitorManager statusMonitorManager) {
        this.statusMonitorManager = statusMonitorManager;
    }

    @Autowired
    public void setMonitorContext(MonitorContext monitorContext) {
        this.monitorContext = monitorContext;
    }

    @Autowired
    public void setServerProperties(MonitorServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }
}
