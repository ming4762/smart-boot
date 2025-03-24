package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.constants.ClientStatusEnum;
import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.event.EventCode;
import com.smart.framework.monitor.server.event.MonitorEvent;
import com.smart.framework.monitor.server.event.data.ClientHealthCheckEventData;
import lombok.Getter;

import java.io.Serial;

/**
 * 客户端状态检测事件
 * @author shizhongming
 * 2021/4/20 8:59 下午
 */
public class ClientHealthCheckEvent extends MonitorEvent<ClientHealthCheckEventData> {
    @Serial
    private static final long serialVersionUID = -7484738673454828717L;

    @Getter
    private final ClientStatusEnum clientStatus;

    public ClientHealthCheckEvent(ClientStatusEnum clientStatus, ClientHealthCheckEventData data, EventCode eventCode, ClientData clientData, Object source) {
        super(data, eventCode, clientData, source);
        this.clientStatus = clientStatus;
    }
}
