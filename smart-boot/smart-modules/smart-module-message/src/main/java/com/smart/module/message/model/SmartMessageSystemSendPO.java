package com.smart.module.message.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 1211913375633237815L;
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

    @TableTenantField
    private Long tenantId;
}