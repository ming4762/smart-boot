package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.MonitorEvent;
import lombok.Getter;

/**
 * 客户端注销事件
 * @author ShiZhongMing
 * 2021/3/22
 * @since 2.0.0
 */
@Getter
public class ClientDeregisterEvent extends MonitorEvent<ClientData> {

    private static final long serialVersionUID = -4665094054496156849L;

    public ClientDeregisterEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.DEREGISTER, clientData, source);
    }
}
