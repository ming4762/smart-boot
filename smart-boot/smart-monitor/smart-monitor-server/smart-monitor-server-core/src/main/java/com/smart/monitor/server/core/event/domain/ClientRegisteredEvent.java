package com.smart.monitor.server.core.event.domain;

import com.smart.monitor.server.common.model.ClientData;
import com.smart.monitor.server.core.constants.BuiltInEventCodeEnum;
import com.smart.monitor.server.core.event.MonitorEvent;
import lombok.Getter;

/**
 * 客户端注册事件
 * @author ShiZhongMing
 * 2021/3/22 10:21
 * @since 1.0
 */
@Getter
public class ClientRegisteredEvent extends MonitorEvent<Object> {


    private static final long serialVersionUID = -2039153255982071800L;

    public ClientRegisteredEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.REGISTER, clientData, source);
    }
}
