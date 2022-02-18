package com.smart.monitor.server.core.notify;

import com.smart.monitor.server.core.event.MonitorEvent;
import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2022/2/18
 * @since 2.0.0
 */
public abstract class AbstractEventNotifier implements EventNotifier {

    /**
     * 进行通知
     * @param event 事件信息
     */
    protected abstract void doNotify(@NonNull MonitorEvent<?> event);

    @Override
    public void notify(@NonNull MonitorEvent<?> event) {
        if (this.shouldNotify(event)) {
            this.doNotify(event);
        }
    }

    /**
     * 是否需要通知
     * @param event 事件信息
     * @return 是否需要通知
     */
    protected boolean shouldNotify(@NonNull MonitorEvent<?> event) {
        return event.getClientData().getNotifyEventCodes().contains(event.getCode().getCode());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
