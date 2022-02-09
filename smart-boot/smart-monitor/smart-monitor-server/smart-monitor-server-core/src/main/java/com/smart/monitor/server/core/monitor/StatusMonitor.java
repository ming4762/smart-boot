package com.smart.monitor.server.core.monitor;

import com.smart.monitor.server.common.model.ClientData;
import org.springframework.core.Ordered;

/**
 * 状态检测接口
 * @author ShiZhongMing
 * 2021/3/22 14:31
 * @since 1.0
 */
public interface StatusMonitor extends Ordered {

    /**
     * 状态检测
     * @param repositoryData 客户端数据
     * @return 是否继续执行
     */
    boolean monitor(ClientData repositoryData);

    /**
     * 是否支持检测
     * @param repositoryData 客户端数据
     * @return 是否支持检测
     */
    default boolean support(ClientData repositoryData) {
        return true;
    }
}
