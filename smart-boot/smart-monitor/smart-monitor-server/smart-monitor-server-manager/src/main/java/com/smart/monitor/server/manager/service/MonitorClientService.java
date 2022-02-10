package com.smart.monitor.server.manager.service;

import com.smart.monitor.server.common.model.ClientData;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/2/10
 * @since 2.0.0
 */
public interface MonitorClientService {

    /**
     * 查询注册客户端列表
     * @param active 是否只查询激活的
     * @return 客户端列表
     */
    @NonNull
    List<ClientData> listRegisterClient(boolean active);

    /**
     * 查询用户关联客户端列表
     * @param userId 用户ID
     * @param active 是否激活
     * @return 客户端列表
     */
    @NonNull
    List<ClientData> listUserClient(@NonNull Long userId, boolean active);
}
