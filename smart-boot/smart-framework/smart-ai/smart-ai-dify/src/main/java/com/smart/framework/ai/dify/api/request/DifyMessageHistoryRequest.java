package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * 获取会话历史消息
 * @author shizhongming
 * 2025/2/10 21:01
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DifyMessageHistoryRequest {

    /**
     * 会话 ID
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     * 用户标识，用于定义终端用户的身份，必须和发送消息接口传入 user 保持一致。
     */
    private String user;

    /**
     * 当前页第一条聊天记录的 ID，默认 null
     */
    @JsonProperty("first_id")
    private String firstId;

    /**
     * 一次请求返回多少条聊天记录，默认 20 条。
     */
    private Integer limit;
}
