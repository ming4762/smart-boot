package com.smart.monitor.actuator.druid.constants;

/**
 * @author ShiZhongMing
 * 2021/4/1 17:00
 * @since 1.0
 */
public interface EndPointIdConstant {

    /**
     * 数据源端点
     */
    String DATASOURCE_END_POINT = "druidDatasource";

    /**
     * druid SQL端点
     */
    String DRUID_SQL_END_POINT = "druidSql";

    /**
     * 慢SQL端点
     */
    String DRUID_SLOW_SQL_END_POINT = "druidSlowSql";

    /**
     * druid防护墙端点
     */
    String DRUID_WALL_END_POINT = "druidWall";
}
