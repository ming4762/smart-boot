package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/3/22 16:08
 * @since 1.0
 */
public class ClientUpEvent extends MonitorEvent<Object> {
    @Serial
    private static final long serialVersionUID = 6903931085396130599L;

    public ClientUpEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.UP, clientData, source);
    }
}
