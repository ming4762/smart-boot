package com.smart.monitor.server.core.event;

import com.smart.monitor.server.common.model.ClientData;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 事件基类
 * @author shizhongming
 * 2021/3/21 5:02 下午
 */
@Getter
public class MonitorEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = -7020603083672803537L;

    private final EventCode code;

    private final ClientData clientData;

    private final T data;

    public MonitorEvent(T data, EventCode code, ClientData clientData, Object source) {
        super(source);
        this.code = code;
        this.clientData = clientData;
        this.data = data;
    }
}
