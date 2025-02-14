package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.constants.ResponseModeEnum;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 聊天消息请求 blocking 模式
 * @author shizhongming
 * 2025/2/8 20:10
 * @since 5.0.0
 */
@Getter
@Setter
public class ChatMessagesApiParameter extends ChatMessagesRequest {

    /**
     * 响应模式，默认 streaming 流式模式（推荐）。基于 SSE（Server-Sent Events）实现类似打字机输出方式的流式返回。
     */
    @JsonProperty("response_mode")
    @NonNull
    @JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
    private ResponseModeEnum responseMode;

}
