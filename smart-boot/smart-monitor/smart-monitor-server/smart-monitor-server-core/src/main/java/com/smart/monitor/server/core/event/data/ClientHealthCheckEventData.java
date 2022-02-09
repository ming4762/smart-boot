package com.smart.monitor.server.core.event.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 客户端状态检测事件数据
 * @author ShiZhongMing
 * 2021/4/21 16:52
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class ClientHealthCheckEventData implements Serializable {
    private static final long serialVersionUID = -487652892557040179L;

    private final long useTime;

    private final String errorMessage;

    private final ClientHealthResult clientHealthResult;
}
