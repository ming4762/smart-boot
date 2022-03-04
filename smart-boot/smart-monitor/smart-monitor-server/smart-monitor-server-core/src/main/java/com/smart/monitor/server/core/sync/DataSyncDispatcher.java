package com.smart.monitor.server.core.sync;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.common.sync.MonitorDataSync;
import com.smart.monitor.server.core.client.repository.ClientRepository;
import com.smart.monitor.server.core.task.TaskSchedulerProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据同步调度器
 * @author ShiZhongMing
 * 2022/2/22
 * @since 2.0.0
 */
public class DataSyncDispatcher implements ApplicationListener<ApplicationReadyEvent> {

    private final TaskSchedulerProvider taskSchedulerProvider;

    private final ClientRepository clientRepository;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public DataSyncDispatcher(TaskSchedulerProvider taskSchedulerProvider, ClientRepository clientRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.taskSchedulerProvider = taskSchedulerProvider;
        this.clientRepository = clientRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        List<MonitorDataSync> monitorDataSyncList = Arrays.stream(event.getApplicationContext().getBeanNamesForType(MonitorDataSync.class))
                .map(name -> event.getApplicationContext().getBean(name, MonitorDataSync.class))
                .sorted(Comparator.comparing(Ordered::getOrder))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(monitorDataSyncList)) {
            this.taskSchedulerProvider.getTaskScheduler()
                    .scheduleWithFixedDelay(() -> this.doSync(monitorDataSyncList), 60L * 1000);
        }
    }

    protected void doSync(@NonNull List<MonitorDataSync> monitorDataSyncList) {
        // 获取所有客户端
        Collection<ClientData> clientDataCollection = this.clientRepository.findAll(true);
        if (CollectionUtils.isEmpty(clientDataCollection)) {
            return;
        }
        clientDataCollection.forEach(clientData -> monitorDataSyncList.forEach(monitorDataSync -> {
            if (monitorDataSync.support(clientData)) {
                this.threadPoolTaskExecutor.execute(() -> monitorDataSync.sync(clientData));
            }
        }));
    }
}
