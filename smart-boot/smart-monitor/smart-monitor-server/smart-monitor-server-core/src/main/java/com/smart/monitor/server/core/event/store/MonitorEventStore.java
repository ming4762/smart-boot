package com.smart.monitor.server.core.event.store;

import com.smart.monitor.server.core.event.EventCode;
import com.smart.monitor.server.core.event.MonitorEvent;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 事件存储器
 * @author ShiZhongMing
 * 2021/3/23
 * @since 2.0.0
 */
public interface MonitorEventStore extends Ordered {

    /**
     * 存储事件信息
     * @param event 事件
     */
    void serialize(MonitorEvent<?> event);

    /**
     * 查询事件信息
     * @param applicationCode 客户端编码
     * @param clientId 客户端ID
     * @param eventCode 事件编码
     * @return 事件列表
     */
    List<?> list(@Nullable String applicationCode, @Nullable String clientId, @Nullable EventCode eventCode);

    /**
     * 时间存储器的顺序
     * @return 顺序
     */
    @Override
    default int getOrder() {
        return 0;
    }
}
