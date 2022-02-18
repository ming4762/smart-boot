package com.smart.monitor.server.common.client;

import com.smart.monitor.server.common.model.ClientManagerData;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author ShiZhongMing
 * 2021/3/22 9:29
 * @since 1.0
 */
public interface ClientManagerProvider {

    /**
     * 通过code获取客户端信息
     * @param applicationName 客户端名称
     * @return 客户端信息
     */
    @Nullable
    ClientManagerData getByName(@NonNull String applicationName);

}
