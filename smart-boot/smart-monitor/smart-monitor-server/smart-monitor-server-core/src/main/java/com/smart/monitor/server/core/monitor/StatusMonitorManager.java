package com.smart.monitor.server.core.monitor;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.model.ClientId;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.task.TaskSchedulerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 客户端状态监听管理器
 * @author shizhongming
 * 2021/3/22 10:11 下午
 */
@Slf4j
public class StatusMonitorManager implements ApplicationContextAware, DisposableBean {

    private final TaskSchedulerProvider taskSchedulerProvider;

    private ClientRepository clientRepository;
    /**
     * 状态监听列表
     */
    private List<? extends StatusMonitor> statusMonitorList;

    private ApplicationContext applicationContext;

    /**
     * 存储执行的定时任务信息
     */
    private final Map<ClientId, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();


    public StatusMonitorManager(TaskSchedulerProvider taskSchedulerProvider) {
        this.taskSchedulerProvider = taskSchedulerProvider;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 验证客户端的健康状态
     * @param clientId 客户端ID
     */
    protected void monitorClient(@NonNull ClientId clientId) {
        final ClientData repositoryData = this.clientRepository.findById(clientId, false);
        log.debug("status monitor run");
        for (StatusMonitor statusMonitor : this.statusMonitorList) {
            if (statusMonitor.support(repositoryData)) {
                boolean result = statusMonitor.monitor(repositoryData);
                if (!result) {
                    break;
                }
            }
        }
    }

    private synchronized void initStatusMonitor() {
        if (this.statusMonitorList == null) {
            this.statusMonitorList = Arrays.stream(applicationContext.getBeanNamesForType(StatusMonitor.class))
                    .map(item -> applicationContext.getBean(item, StatusMonitor.class))
                    .sorted(Comparator.comparingInt(Ordered::getOrder))
                    .toList();
        }
    }

    /**
     * 添加定时任务
     * @param repositoryData 客户端信息
     */
    public void addScheduled(@NonNull ClientData repositoryData) {
        this.initStatusMonitor();
        if (scheduledFutureMap.containsKey(repositoryData.getId())) {
            log.warn("task is running");
            return;
        }
        log.debug("client status task is start, client code: {}, client id : {}", repositoryData.getApplication().getApplicationName(), repositoryData.getId().getValue());
        // 启动后延迟5秒执行
        final Instant instant = Instant.now().plusSeconds(5);
        final ScheduledFuture<?> scheduledTask = this.taskSchedulerProvider.getTaskScheduler().scheduleWithFixedDelay(() -> this.monitorClient(repositoryData.getId()), instant, repositoryData.getStatusInterval());
        this.scheduledFutureMap.put(repositoryData.getId(), scheduledTask);
    }

    /**
     * 移除定时任务
     * @param clientId ID
     */
    public void removeScheduled(@NonNull ClientId clientId) {
        // 获取定时任务
        final ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(clientId);
        if (scheduledFuture == null) {
            log.warn("remove status task fail, task not found, client id: {}", clientId.getValue());
            return;
        }
        if (!scheduledFuture.isDone()) {
            scheduledFuture.cancel(true);
            log.debug("remove status task success, client id: {}", clientId.getValue());
        }
        this.scheduledFutureMap.remove(clientId);
    }

    @Override
    public void destroy() {
        this.scheduledFutureMap.values().forEach(item -> {
            if (item != null && !item.isDone()) {
                item.cancel(true);
            }
        });
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
