package com.smart.monitor.server.core.event;

import org.springframework.lang.NonNull;

/**
 * 监控事件发布接口
 * @author shizhongming
 * 2021/3/21 5:03 下午
 */
public interface MonitorEventPublisher {

    /**
     * 发布事件
     * @param monitorEvent 事件信息
     */
    void publishEvent(@NonNull MonitorEvent<?> monitorEvent);
}
