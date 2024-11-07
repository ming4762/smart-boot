package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;

import java.io.Serial;

/**
 * 客户端下线事件
 * @author ShiZhongMing
 * 2021/3/22 10:27
 * @since 1.0
 */
public class ClientOfflineEvent extends MonitorEvent<ClientData> {
    @Serial
    private static final long serialVersionUID = -43019174170748941L;

    public ClientOfflineEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.OFFLINE, clientData, source);
    }
}
