package com.smart.monitor.server.core.event.store;

import com.smart.monitor.server.core.event.MonitorEvent;
import com.smart.monitor.server.core.event.MonitorEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
@Slf4j
public class MonitorEventStoreHandler implements ApplicationContextAware {

    private List<MonitorEventStore> monitorEventStoreList = new ArrayList<>(0);

    @MonitorEventListener
    public void handler(MonitorEvent<?> event) {
        Set<String> serializeEventCodes = event.getClientData().getSerializeEventCodes();
        if (serializeEventCodes.contains("ALL") || serializeEventCodes.contains(event.getCode().getCode())) {
            for (MonitorEventStore eventStore : this.monitorEventStoreList) {
                try {
                    eventStore.serialize(event);
                } catch (Exception e) {
                    log.error("Event serialization failed，application name：{}, client id: {}, event code: {}", event.getClientData().getApplication().getApplicationName(), event.getClientData().getId().getValue(), event.getCode().getCode(), e);
                }
            }
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        monitorEventStoreList = Arrays.stream(applicationContext.getBeanNamesForType(MonitorEventStore.class))
                .map(name -> applicationContext.getBean(name, MonitorEventStore.class))
                .sorted(Comparator.comparingInt(MonitorEventStore::getOrder))
                .collect(Collectors.toList());
    }
}
