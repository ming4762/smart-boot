package com.smart.monitor.server.core.notify;

import com.smart.monitor.server.core.event.MonitorEvent;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

/**
 * 通知接口
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
public interface EventNotifier extends Ordered {

    /**
     * 事件通知接口
     * @param event 事件信息
     */
    void notify(@NonNull MonitorEvent<?> event);
}
