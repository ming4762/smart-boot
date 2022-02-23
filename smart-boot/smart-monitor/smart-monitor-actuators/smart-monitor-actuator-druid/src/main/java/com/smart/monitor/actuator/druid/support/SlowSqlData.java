package com.smart.monitor.actuator.druid.support;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存储慢SQL数据
 * @author ShiZhongMing
 * 2021/4/2 11:01
 * @since 1.0
 */
@Getter
@Setter
@Builder
@ToString
public class SlowSqlData implements Serializable {
    private static final long serialVersionUID = -7485086887117999458L;

    private Long id;

    private String sql;

    private String parameter;

    private long useMillis;

    private String datasourceName;

    /**
     * 时间戳
     */
    private long timestamp;
}
