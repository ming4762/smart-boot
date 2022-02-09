package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.MonitorEvent;
import lombok.Getter;

/**
 * 客户端注册刷新事件
 * @author ShiZhongMing
 * 2021/3/22 10:23
 * @since 1.0
 */
@Getter
public class ClientRegisteredUpdateEvent extends MonitorEvent<Object> {
    private static final long serialVersionUID = 8475120709084451790L;

    public ClientRegisteredUpdateEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.REGISTERED_UPDATE, clientData, source);
    }
}
