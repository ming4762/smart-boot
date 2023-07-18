package com.smart.module.api.message.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 消息发送结果
 * @author zhongming4762
 * 2023/7/13 19:14
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendDTO implements Serializable {

    /**
     * 系统消息发送结果
     */
    private SystemMessageSendDTO systemMessageSendResult;

    private SmsSendDTO smsSendResult;

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class SystemMessageSendDTO implements Serializable {
        private Long messageId;
    }
}
