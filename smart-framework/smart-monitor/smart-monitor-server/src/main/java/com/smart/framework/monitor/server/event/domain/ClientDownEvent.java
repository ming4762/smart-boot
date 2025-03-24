package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/3/22 16:06
 * @since 1.0
 */
public class ClientDownEvent extends MonitorEvent<Object> {
    @Serial
    private static final long serialVersionUID = 960886837520615590L;

    public ClientDownEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.DOWN, clientData, source);
    }
}
