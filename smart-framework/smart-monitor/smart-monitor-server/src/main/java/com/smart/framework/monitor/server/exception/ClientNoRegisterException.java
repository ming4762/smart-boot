package com.smart.framework.monitor.server.exception;

import com.smart.framework.commons.core.exception.BusinessException;
import com.smart.framework.monitor.server.common.model.ClientId;

import java.io.Serial;

/**
 * 客户端未注册
 * @author ShiZhongMing
 * 2021/3/25 15:47
 * @since 1.0
 */
public class ClientNoRegisterException extends BusinessException {
    @Serial
    private static final long serialVersionUID = -7900322238877336121L;

    public ClientNoRegisterException(ClientId clientId) {
        this(String.format("client is not register, client id: %s", clientId.getValue()));
    }

    public ClientNoRegisterException(String message) {
        super(message);
    }
}
