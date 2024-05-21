package com.smart.module.api.message.dto;

import lombok.*;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = 3896391008459249751L;
    /**
     * 系统消息发送结果
     */
    private SystemMessageSendDTO systemMessageSendResult;

    private SmsSendDTO smsSendResult;

    private DingtalkWorkNoticeSendDTO dingtalkWorkNoticeSendResult;

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class SystemMessageSendDTO implements Serializable {
        @Serial
        private static final long serialVersionUID = -5723191833115453567L;
        private Long messageId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    public static class DingtalkWorkNoticeSendDTO implements Serializable {
        @Serial
        private static final long serialVersionUID = 5019682880055330140L;
        private String taskId;
    }
}
