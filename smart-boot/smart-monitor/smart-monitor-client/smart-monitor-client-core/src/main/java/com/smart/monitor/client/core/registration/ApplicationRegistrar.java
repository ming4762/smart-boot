package com.smart.monitor.client.core.registration;

import org.springframework.lang.Nullable;

/**
 * 客户端注册
 * @author shizhongming
 * 2021/3/18
 * @since 2.0.0
 */
public interface ApplicationRegistrar {

    /**
     * 注册客户端
     * @return 是否注册成功
     */
    boolean register();

    /**
     * 注销客户端
     */
    void deregister();

    /**
     * 获取注册的IP
     * @return 注册的ID
     */
    @Nullable
    String getRegisteredId();
}
