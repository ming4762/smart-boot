package com.smart.message.manager.pojo.paramteter;

import com.smart.message.manager.constants.MessagePriorityEnum;
import com.smart.message.manager.constants.MessageTypeEnum;
import com.smart.message.manager.constants.ReceiveUserTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
* smart_message_system - 系统消息表
* @author SmartCodeGenerator
* 2023年7月6日 下午4:53:46
*/
@Getter
@Setter
@ToString
public class SmartMessageSystemSaveUpdateParameter implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 标题
    */
    @NotNull(message = "标题不能为空")
    private String title;
    /**
    * 摘要
    */
    private String abstractContent;
    /**
    * 内容
    */
    @NotNull(message = "标题不能为空")
    private String content;
    /**
    * 消息类型
    */
    @NotNull(message = "消息类型不能为空")
    private MessageTypeEnum messageType;
    /**
    * 优先级
    */
    @NotNull(message = "优先级不能为空")
    private MessagePriorityEnum priority;
    /**
    * 通知用户类型
    */
    @NotNull(message = "通知用户类型不能为空")
    private ReceiveUserTypeEnum receiveUserType;

    private List<Long> userIds;

    /**
    * 
    */
    private Boolean useYn;

}