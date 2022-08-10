package com.smart.monitor.server.core.event.store;

import com.smart.monitor.server.core.event.EventCode;
import com.smart.monitor.server.core.event.MonitorEvent;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * 默认的内存时间存储器
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
public class DefaultMemoryMonitorEventStore implements MonitorEventStore {

    private final CircularFifoQueue<MonitorEvent<?>> monitorEvents = new CircularFifoQueue<>(500);

    @Override
    public void serialize(MonitorEvent<?> event) {
        monitorEvents.add(event);
    }

    @Override
    public List<MonitorEvent<?>> list(@Nullable String applicationCode, @Nullable String clientId, @Nullable EventCode eventCode) {
        Stream<MonitorEvent<?>> eventStream = this.monitorEvents.stream();
        if (StringUtils.hasLength(applicationCode)) {
            eventStream = eventStream.filter(item -> applicationCode.equals(item.getClientData().getApplication().getApplicationName()));
        }
        if (StringUtils.hasLength(clientId)) {
            eventStream = eventStream.filter(item -> clientId.equals(item.getClientData().getId().getValue()));
        }
        if (eventCode != null) {
            eventStream = eventStream.filter(item -> eventCode.getCode().equals(item.getCode().getCode()));
        }
        return eventStream.toList();
    }
}
