package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.smart.crud.model.BaseModel;
import lombok.*;

import java.io.Serial;

/**
* monitor_client_slow_sql - 客户端慢SQL
* @author GCCodeGenerator
* 2022-2-24 10:57:04
*/
@Getter
@Setter
@TableName("monitor_client_slow_sql")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorClientSlowSqlPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 4817793987516339679L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * application_code - 客户端编码

    */
    private String applicationCode;

    /**
    * client_id - 客户端ID
    */
    private String clientId;

    private String clientUrl;

    /**
    * datasource_name - 数据源
    */
    private String datasourceName;

    /**
    * sql_id - SQL ID
    */
    private Long sqlId;

    /**
    * sql_text - SQL
    */
    private String sqlText;

    /**
    * parameter - 参数
    */
    private String parameter;

    /**
    * use_millis - 执行用时(ms)
    */
    private Long useMillis;

    /**
    * timestamp - 时间戳
    */
    private Long timestamp;

}
