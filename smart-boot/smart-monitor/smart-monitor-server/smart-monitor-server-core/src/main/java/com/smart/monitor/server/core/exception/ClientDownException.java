package com.smart.monitor.server.core.exception;

import com.smart.commons.core.exception.BusinessException;
import com.smart.monitor.server.common.model.ClientId;

/**
 * @author ShiZhongMing
 * 2021/3/25 15:54
 * @since 1.0
 */
public class ClientDownException extends BusinessException {

    private static final long serialVersionUID = -8663434862331832072L;

    public ClientDownException(ClientId clientId) {
        super(String.format("client is down, client id: %s", clientId.getValue()));
    }
}
