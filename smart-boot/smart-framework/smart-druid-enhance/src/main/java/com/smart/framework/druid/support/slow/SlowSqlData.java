package com.smart.framework.druid.support.slow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;

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
    @Serial
    private static final long serialVersionUID = -7485086887117999458L;

    private Long sqlId;

    private String dbType;

    private String sql;

    private String parameter;

    private Duration useTime;

    private String datasourceName;

    /**
     * 查询的列信息
     */
    private List<String> columnList;

    /**
     * 时间戳
     */
    private long timestamp;
}
