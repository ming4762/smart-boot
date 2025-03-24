package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;
import lombok.Getter;

import java.io.Serial;

/**
 * 客户端注销事件
 * @author ShiZhongMing
 * 2021/3/22
 * @since 2.0.0
 */
@Getter
public class ClientDeregisterEvent extends MonitorEvent<ClientData> {

    @Serial
    private static final long serialVersionUID = -4665094054496156849L;

    public ClientDeregisterEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.DEREGISTER, clientData, source);
    }
}
