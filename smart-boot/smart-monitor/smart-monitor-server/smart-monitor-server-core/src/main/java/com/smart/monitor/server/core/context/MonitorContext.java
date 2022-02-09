package com.smart.monitor.server.core.context;

import com.smart.monitor.server.core.event.MonitorEventPublisher;
import org.springframework.context.ApplicationContext;

/**
 * @author shizhongming
 * 2021/3/21 5:04 下午
 */
public interface MonitorContext extends MonitorEventPublisher {


    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    ApplicationContext getApplicationContext();
}
