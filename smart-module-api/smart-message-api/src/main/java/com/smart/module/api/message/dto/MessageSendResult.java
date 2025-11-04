package com.smart.module.api.message.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息发送结果
 * @author shizhongming
 * 2025/9/26 13:58
 * @since 5.0.0
 */
@Getter
@Setter
public class MessageSendResult implements Serializable {
    private boolean success;
    public MessageSendResult() {
        this.success = true;
    }
}
