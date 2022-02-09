package com.smart.monitor.server.core.constants;

import lombok.Getter;

/**
 * 客户端地址枚举
 * @author ShiZhongMing
 * 2022/2/9
 * @since 2.0.0
 */
@Getter
public enum ClientUrlEnum {

    /**
     * 健康状态检测
     */
    HEALTH("/actuator/health")
    ;

    private final String url;

    ClientUrlEnum(String url) {
        this.url = url;
    }
}
