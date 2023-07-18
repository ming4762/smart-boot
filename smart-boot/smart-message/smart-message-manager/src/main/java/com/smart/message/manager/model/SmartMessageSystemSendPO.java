package com.smart.message.manager.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
* smart_message_system_send - 系统消息发送阅读记录
* @author SmartCodeGenerator
* 2023年7月14日 下午6:44:49
*/
@Getter
@Setter
@TableName("smart_message_system_send")
public class SmartMessageSystemSendPO extends BaseModelUserTime {

    /**
    * id - id
    */
    private Long id;

    /**
    * message_id - messageId
    */
    private Long messageId;

    /**
    * user_id - userId
    */
    private Long userId;

    /**
    * read_yn - 是否已读
    */
    private Boolean readYn;

    /**
    * read_time - 阅读时间
    */
    private LocalDateTime readTime;

    /**
    * star_yn - 是否标星
    */
    private Boolean starYn;

}