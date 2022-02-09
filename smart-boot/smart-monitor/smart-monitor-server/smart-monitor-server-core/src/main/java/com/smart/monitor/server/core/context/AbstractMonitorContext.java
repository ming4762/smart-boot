package com.smart.monitor.server.core.context;

import com.smart.monitor.server.core.event.MonitorEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * @author shizhongming
 * 2021/3/21 5:44 下午
 */
public abstract class AbstractMonitorContext implements MonitorContext, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * 发布事件
     * @param monitorEvent 事件信息
     */
    @Override
    public void publishEvent(@NonNull MonitorEvent<?> monitorEvent) {
        this.applicationContext.publishEvent(monitorEvent);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
