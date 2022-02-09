package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.data.ClientHealthCheckEventData;

/**
 * 客户端状态检测UP事件
 * @author ShiZhongMing
 * 2021/4/21 15:29
 * @since 1.0
 */
public class ClientHealthCheckUpEvent extends ClientHealthCheckEvent {
    private static final long serialVersionUID = 1221567142192215346L;

    public ClientHealthCheckUpEvent(ClientData clientData, ClientHealthCheckEventData data, Object source) {
        super(ClientStatusEnum.UP, data, BuiltInEventCodeEnum.HEALTH_CHECK_UP, clientData, source);
    }
}
