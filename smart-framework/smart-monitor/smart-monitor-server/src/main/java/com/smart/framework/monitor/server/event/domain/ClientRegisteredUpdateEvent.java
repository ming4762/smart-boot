package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;
import lombok.Getter;

import java.io.Serial;

/**
 * 客户端注册刷新事件
 * @author ShiZhongMing
 * 2021/3/22 10:23
 * @since 1.0
 */
@Getter
public class ClientRegisteredUpdateEvent extends MonitorEvent<Object> {
    @Serial
    private static final long serialVersionUID = 8475120709084451790L;

    public ClientRegisteredUpdateEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.REGISTERED_UPDATE, clientData, source);
    }
}
