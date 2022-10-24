package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.constants.ClientStatusEnum;
import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.data.ClientHealthCheckEventData;

import java.io.Serial;

/**
 * 客户端健康检测失败事件
 * @author ShiZhongMing
 * 2021/4/21 15:24
 * @since 1.0
 */
public class ClientHealthCheckErrorEvent extends ClientHealthCheckEvent {
    @Serial
    private static final long serialVersionUID = -919092264461866901L;

    public ClientHealthCheckErrorEvent(ClientData clientData, ClientHealthCheckEventData data, Object source) {
        super(ClientStatusEnum.ERROR, data, BuiltInEventCodeEnum.HEALTH_CHECK_ERROR, clientData, source);
    }
}
