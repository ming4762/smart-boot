package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.smart.crud.model.BaseModel;
import lombok.*;

import java.time.LocalDateTime;

/**
* monitor_client_http_trace - 客户端HttpTrace
* @author GCCodeGenerator
* 2022-3-25 13:19:44
*/
@Getter
@Setter
@TableName("monitor_client_http_trace")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorClientHttpTracePO extends BaseModel {

    private static final long serialVersionUID = -3412239413431611188L;

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
    * http_method - 请求方式
    */
    private String httpMethod;

    /**
    * time_taken - 用时(毫秒)
    */
    private Long timeTaken;

    /**
    * url - 请求地址
    */
    private String url;

    /**
    * response_status - 状态
    */
    private Integer responseStatus;

    /**
    * timestamp - 时间戳
    */
    private Long timestamp;

    private LocalDateTime createTime;

    private String data;

}
