package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.*;
import org.springframework.boot.logging.LogLevel;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* monitor_client_log - 客户端日志
* @author GCCodeGenerator
* 2022-3-3
*/
@Getter
@Setter
@TableName("monitor_client_log")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonitorClientLogPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 5153677950410116677L;
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

    /**
    * create_time - createTime
    */
    private LocalDateTime createTime;

    /**
    * thread_name - 线程名
    */
    private String threadName;

    /**
    * logger_name - 日志名
    */
    private String loggerName;

    /**
    * timestamp - 时间戳
    */
    private Long timestamp;

    /**
    * level - 日志级别
    */
    private LogLevel level;

    /**
    * log_text - 日志内容
    */
    private String logText;

}
