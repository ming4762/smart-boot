package com.smart.framework.monitor.server.event.domain;

import com.smart.framework.monitor.server.common.model.ClientData;
import com.smart.framework.monitor.server.constants.BuiltInEventCodeEnum;
import com.smart.framework.monitor.server.event.MonitorEvent;
import lombok.Getter;

import java.io.Serial;

/**
 * 客户端注册事件
 * @author ShiZhongMing
 * 2021/3/22 10:21
 * @since 1.0
 */
@Getter
public class ClientRegisteredEvent extends MonitorEvent<Object> {


    @Serial
    private static final long serialVersionUID = -2039153255982071800L;

    public ClientRegisteredEvent(ClientData clientData, Object source) {
        super(null, BuiltInEventCodeEnum.REGISTER, clientData, source);
    }
}
