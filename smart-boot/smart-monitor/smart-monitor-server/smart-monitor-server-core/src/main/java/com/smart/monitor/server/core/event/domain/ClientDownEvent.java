package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.MonitorEvent;

/**
 * @author ShiZhongMing
 * 2021/3/22 16:06
 * @since 1.0
 */
public class ClientDownEvent extends MonitorEvent<Object> {
    private static final long serialVersionUID = 960886837520615590L;

    public ClientDownEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.DOWN, clientData, source);
    }
}
