package com.smart.monitor.server.core.monitor.status;

import com.smart.commons.core.utils.JsonUtils;
import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.client.ClientProcessor;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.client.request.ClientWebProxy;
import com.smart.monitor.server.core.constants.ClientUrlEnum;
import com.smart.monitor.server.core.event.MonitorEventPublisher;
import com.smart.monitor.server.core.event.data.ClientHealthCheckEventData;
import com.smart.monitor.server.core.event.data.ClientHealthResult;
import com.smart.monitor.server.core.event.domain.*;
import com.smart.monitor.server.core.monitor.StatusMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * health 状态检测
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
@Slf4j
public class HealthStatusMonitor implements StatusMonitor {

    private final ClientWebProxy clientWebProxy;

    private final MonitorEventPublisher monitorEventPublisher;

    private final ClientRepository clientRepository;

    private final ClientProcessor clientProcessor;

    public HealthStatusMonitor(ClientWebProxy clientWebProxy, MonitorEventPublisher monitorEventPublisher, ClientRepository clientRepository, ClientProcessor clientProcessor) {
        this.clientWebProxy = clientWebProxy;
        this.monitorEventPublisher = monitorEventPublisher;
        this.clientRepository = clientRepository;
        this.clientProcessor = clientProcessor;
    }

    @Override
    public boolean monitor(ClientData repositoryData) {
        if (repositoryData == null) {
            return true;
        }
        log.debug("start check client health, application name: {}, client id: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
        String[] healthData = new String[1];
        long startTimestamp = System.nanoTime();
        try {
            this.clientWebProxy.forward(repositoryData.getId(), data -> ClientWebProxy.ForwardRequest.builder()
                    .uri(ClientUrlEnum.HEALTH.getUrl())
                    .httpMethod(HttpMethod.GET)
                    .build(),
                    result -> healthData[0] = result, false, String.class
            );
        } catch (Exception e) {
            log.warn("client health monitor error, application name: {}, client id: {}, error message: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue(), e.getMessage());
            // 发布状态检测失败事件
            this.monitorEventPublisher.publishEvent(new ClientHealthCheckErrorEvent(repositoryData, new ClientHealthCheckEventData(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTimestamp), e.getMessage(), null), this));
        }
        ClientHealthResult health = null;
        if (healthData[0] != null) {
            health = JsonUtils.parse(healthData[0], ClientHealthResult.class);
        }
        if (health != null) {
            ClientHealthCheckEventData eventData = new ClientHealthCheckEventData(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTimestamp), null, health);
            // 状态检测返回结果
            // 判断是UP还是DOWN
            if (ClientStatusEnum.UP.equals(health.getStatus())) {
                this.monitorEventPublisher.publishEvent(new ClientHealthCheckUpEvent(repositoryData, eventData, this));
            } else {
                this.monitorEventPublisher.publishEvent(new ClientHealthCheckDownEvent(repositoryData, eventData, this));
            }
        }
        if (health == null || !ClientStatusEnum.UP.equals(health.getStatus())) {
            // 客户端检测失败或者已下线
            log.debug("client health monitor fail, application name: {}, client id: {}, message: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue(), JsonUtils.toJsonString(health));
            // 判断之前的状态是否是UP状态，如果是执行下线操作
            if (ClientStatusEnum.UP.equals(repositoryData.getStatus())) {
                log.info("client id down, client code: {}, id: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
                // 发布下线事件
                this.monitorEventPublisher.publishEvent(new ClientDownEvent(repositoryData, this));
                // 执行下线
                this.clientRepository.down(repositoryData.getId());
            }
            // 判断是否移除客户端
            // 判断是否移除
            if (Duration.between(repositoryData.getTimestamp(), Instant.now()).compareTo(repositoryData.getOfflineInterval()) > 0) {
                log.info("client offline, application name: {}, client id: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
                // 事件间隔已经超过最大时间间隔,发布下线事件
                this.monitorEventPublisher.publishEvent(new ClientOfflineEvent(repositoryData, this));
                // 移除客户端
                this.clientProcessor.removeClient(repositoryData.getId());
            }
            // 返回false，不再执行后续的检测
            return false;
        }
        // 更新时间戳
        this.clientRepository.updateTimestamp(repositoryData.getId());
        log.debug("update client timestamp, client id: {}", repositoryData.getId().getValue());
        log.info("health monitor success, application name: {}, client id: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
        // 如果之前不是UP状态，重新修改为UP状态
        if (!ClientStatusEnum.UP.equals(repositoryData.getStatus())) {
            if (ClientStatusEnum.DOWN.equals(repositoryData.getStatus())) {
                // 重新上线
                // 之前是下线，执行上线操作
                this.monitorEventPublisher.publishEvent(new ClientUpEvent(repositoryData, this));
                log.info("client id up, application name: {}, client id: {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
            }
            this.clientRepository.up(repositoryData.getId());
        }
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
