package com.smart.module.message.pojo.dto;

import com.smart.module.api.message.dto.MessageSendResult;
import lombok.Getter;
import lombok.Setter;

/**
 * 系统消息发送结果
 * @author shizhongming
 * 2025/9/26 13:59
 * @since 5.0.0
 */
@Getter
@Setter
public class SystemMessageSendResult extends MessageSendResult {

    private Long messageId;
}
