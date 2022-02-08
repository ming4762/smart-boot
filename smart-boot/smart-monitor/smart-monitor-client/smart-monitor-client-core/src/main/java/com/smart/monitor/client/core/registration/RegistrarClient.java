package com.smart.monitor.client.core.registration;

import com.smart.monitor.core.model.Application;
import org.springframework.lang.NonNull;

/**
 * 执行注册接口
 * @author shizhongming
 * 2021/3/21 8:35 上午
 */
public interface RegistrarClient {

    /**
     * 注册客户端
     * @param serverUrl 服务端地址
     * @param application 客户端信息
     * @return 注册ID
     */
    @NonNull
    String register(String serverUrl, Application application);

    /**
     * 注销客户端
     * @param serverUrl 服务端地址
     * @param applicationId 客户端ID
     */
    void deregister(String serverUrl, String applicationId);
}
