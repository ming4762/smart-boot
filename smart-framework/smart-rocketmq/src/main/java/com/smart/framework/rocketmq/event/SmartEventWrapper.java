package com.smart.framework.rocketmq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * 事件包装类，用于在stream中传输
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-11 20:33
 * @since 5.0.0
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmartEventWrapper implements Serializable {

    /**
     * 事件类全限定名
     */
    private String eventClass;

    /**
     * 事件数据 JSON字符串
     */
    private String eventData;

    /**
     * 事件的来源服务
     */
    private String eventSourceService;

    /**
     * 事件ID
     */
    @Builder.Default
    private String eventId = UUID.randomUUID().toString();

    @Builder.Default
    private long timestamp = System.currentTimeMillis();
}
