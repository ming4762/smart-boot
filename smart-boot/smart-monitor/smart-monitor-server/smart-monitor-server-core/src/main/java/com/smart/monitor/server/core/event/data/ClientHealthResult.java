package com.smart.monitor.server.core.event.data;

import com.smart.monitor.server.common.constants.ClientStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 客户端健康检测结果
 * @author shizhongming
 * 2021/4/20 9:06 下午
 */
@Getter
@Setter
public class ClientHealthResult {

    /**
     * 状态
     */
    private ClientStatusEnum status;

    private Map<String, Object> components;
}
