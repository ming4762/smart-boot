package com.smart.monitor.server.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* monitor_event - 事件信息
* @author GCCodeGenerator
* 2022-2-18
*/
@Getter
@Setter
@TableName("monitor_event")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorEventPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 2402591844218914563L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * application_code - 客户端名称
    */
    private String applicationCode;

    /**
    * client_id - 客户端ID
    */
    private String clientId;

    /**
    * event_code - 事件编码
    */
    private String eventCode;

    /**
    * timestamp - 时间戳
    */
    private Long timestamp;

    /**
    * event_message - 事件内容
    */
    private String eventMessage;

    /**
    * create_time - 创建事件
    */
    private LocalDateTime createTime;

}
