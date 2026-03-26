package com.smart.framework.monitor.server.common.client;

import com.smart.framework.monitor.server.common.model.ClientManagerData;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
