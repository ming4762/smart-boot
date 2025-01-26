package com.smart.framework.commons.core.spring;

import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

/**
 * 多时区配置
 * @author shizhongming
 * 2025/1/25 19:12
 * @since 5.0.0
 */
public class MultiTimeZoneConfigurer {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
