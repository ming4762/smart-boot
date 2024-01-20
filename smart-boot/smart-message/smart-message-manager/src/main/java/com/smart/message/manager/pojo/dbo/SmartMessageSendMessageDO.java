package com.smart.message.manager.pojo.dbo;

import com.smart.message.manager.constants.MessagePriorityEnum;
import com.smart.message.manager.constants.MessageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2024/1/20 14:24
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class SmartMessageSendMessageDO implements Serializable {

    /**
     * id - id
     */
    private Long id;

    /**
     * message_id - messageId
     */
    private Long messageId;

    private Long receiveUserId;

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

    private LocalDateTime sendTime;

    private Long sendUserId;

    private String sendUserName;

    /**
     * title - 标题
     */
    private String title;

    /**
     * abstract - 摘要
     */
    private String abstractContent;

    /**
     * message_type - 消息类型
     */
    private MessageTypeEnum messageType;

    /**
     * priority - 优先级
     */
    private MessagePriorityEnum priority;

    /**
     * business_ident - 业务标识位
     */
    private String businessIdent;

    /**
     * business_id - 业务ID
     */
    private Long businessId;
}
