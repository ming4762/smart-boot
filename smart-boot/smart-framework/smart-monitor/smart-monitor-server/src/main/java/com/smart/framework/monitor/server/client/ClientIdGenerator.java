package com.smart.framework.monitor.server.client;

import com.smart.framework.monitor.core.model.Application;
import com.smart.framework.monitor.server.common.model.ClientId;

/**
 * 客户端ID创建接口
 * @author shizhongming
 * 2021/3/21 10:19 下午
 */
public interface ClientIdGenerator {

    /**
     * 创建客户端ID
     * @param application 客户端信息
     * @return 客户端ID
     */
    ClientId create(Application application);
}
