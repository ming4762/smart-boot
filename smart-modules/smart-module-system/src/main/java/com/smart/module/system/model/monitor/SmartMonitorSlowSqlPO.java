package com.smart.module.system.model.monitor;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import lombok.*;

import java.time.ZonedDateTime;

/**
* smart_monitor_slow_sql - 慢sql记录
* @author SmartCodeGenerator
* 2025年3月1日 20:58:15
*/
@Getter
@Setter
@TableName("smart_monitor_slow_sql")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartMonitorSlowSqlPO extends BaseModelCreateUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * sql_id - sql id
    */
    private Long sqlId;

    /**
    * db_type - 数据库类型
    */
    private String dbType;

    /**
    * sql_text - sql语句
    */
    private String sqlText;

    /**
    * parameter - 参数
    */
    private String parameter;

    /**
    * datasource_name - 数据源名称
    */
    private String datasourceName;

    /**
    * column_list - 列名列表
    */
    private String columnList;

    /**
    * timestamp - 时间戳
    */
    private ZonedDateTime timestamp;

    /**
    * use_time - 执行时间（毫秒）
    */
    private Long useTime;

}