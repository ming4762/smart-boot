package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.data.ClientHealthCheckEventData;

/**
 * 客户端状态检测下线事件
 * @author ShiZhongMing
 * 2021/4/21 15:27
 * @since 1.0
 */
public class ClientHealthCheckDownEvent extends ClientHealthCheckEvent {
    private static final long serialVersionUID = 3755990250500536984L;

    public ClientHealthCheckDownEvent(ClientData clientData, ClientHealthCheckEventData data, Object source) {
        super(ClientStatusEnum.DOWN, data, BuiltInEventCodeEnum.HEALTH_CHECK_DOWN, clientData, source);
    }
}
