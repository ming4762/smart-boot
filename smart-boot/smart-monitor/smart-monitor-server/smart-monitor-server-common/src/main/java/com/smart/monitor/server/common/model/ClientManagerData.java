package com.smart.monitor.server.common.model;

import lombok.*;

import java.time.Duration;
import java.util.Set;

/**
 * @author ShiZhongMing
 * 2021/3/22 8:53
 * @since 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientManagerData {

    private Long id;

    /**
     * 客户端编码
     */
    private String applicationName;

    private String name;

    private String remark;


    /**
     * 客户端状态检测间隔事件
     */
    private Duration statusInterval;

    /**
     * 客户端离线检测事件间隔
     */
    private Duration offlineInterval;

    private String token;

    /**
     * 序列化的事件编码
     */
    private Set<String> serializeEventCodes;
}
