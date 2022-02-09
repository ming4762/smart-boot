package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.MonitorEvent;

/**
 * @author ShiZhongMing
 * 2021/3/22 16:08
 * @since 1.0
 */
public class ClientUpEvent extends MonitorEvent<Object> {
    private static final long serialVersionUID = 6903931085396130599L;

    public ClientUpEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.UP, clientData, source);
    }
}
