package com.smart.kettle.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 日志数据库配置信息
 * @author ShiZhongMing
 * 2020/8/24 10:32
 * @since 1.0
 */
@Getter
@Setter
public class LogDatabaseProperties extends DatabaseMetaProperties  {

    private Boolean enable = true;

    public static final String DEFAULT_DB_NAME = "LOG_DB";

    /**
     * 转换日志表名称
     */
    private String transLogTableName;

    /**
     * 步骤日志表名称
     */
    private String stepLogTableName;

    /**
     * metrics 日志表名称
     */
    private String metricsLogTableName;

    private String performanceLogTableName;

    private String channelLogTableName;

    /**
     * job日志
     */
    private String jobLogTableName;

    private String jobEntryLogTableName;
}
