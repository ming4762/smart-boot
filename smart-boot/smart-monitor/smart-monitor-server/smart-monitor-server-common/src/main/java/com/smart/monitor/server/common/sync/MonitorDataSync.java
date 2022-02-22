package com.smart.monitor.server.common.sync;

import com.smart.monitor.server.common.model.ClientData;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

/**
 * 监控数据同步接口
 * @author ShiZhongMing
 * 2021/4/13 13:22
 * @since 1.0
 */
public interface MonitorDataSync extends Ordered {

    /**
     * 数据序列化
     * @param clientData 客户端数据
     * @return 同步日志条数
     */
    Integer sync(@NonNull ClientData clientData);

    /**
     * 指定客户端是否支持此数据同步
     * @param clientData 客户端数据
     * @return 是否支持
     */
    boolean support(@NonNull ClientData clientData);
}
