package com.smart.message.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.crud.mybatis.handler.LongSplitTypeHandler;
import com.smart.message.manager.constants.MessagePriorityEnum;
import com.smart.message.manager.constants.MessageSendStatusEnum;
import com.smart.message.manager.constants.MessageTypeEnum;
import com.smart.message.manager.constants.ReceiveUserTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
* smart_message_system - 系统消息表
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
@Getter
@Setter
@TableName(value = "smart_message_system", autoResultMap = true)
public class SmartMessageSystemPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * title - 标题
    */
    private String title;

    /**
    * content - 内容
    */
    private String content;

    /**
    * abstract - 摘要
    */
    private String abstractContent;

    /**
    * message_type - 消息类型
    */
    private MessageTypeEnum messageType;

    /**
    * send_status - 发布状态
    */
    private MessageSendStatusEnum sendStatus;

    /**
    * priority - 优先级
    */
    private MessagePriorityEnum priority;

    /**
    * receive_user_type - 通知用户类型
    */
    private ReceiveUserTypeEnum receiveUserType;

    /**
     * 发送人ID
     */
    private Long sendUserId;

    /**
    * send_time - 发布时间
    */
    private LocalDateTime sendTime;

    /**
    * cancel_time - 撤销时间
    */
    private LocalDateTime cancelTime;

    /**
    * user_ids - 指定用户ID，已逗号分隔
    */
    @TableField(typeHandler = LongSplitTypeHandler.class)
    private List<Long> userIds;

    /**
    * business_ident - 业务标识位
    */
    private String businessIdent;

    /**
    * business_id - 业务ID
    */
    private Long businessId;

    /**
    * business_data - 业务数据
    */
    private String businessData;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

}