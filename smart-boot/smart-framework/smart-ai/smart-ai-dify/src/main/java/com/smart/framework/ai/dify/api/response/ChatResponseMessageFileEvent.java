package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.constants.ChatFileTypeEnum;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件事件，表示有新文件需要展示
 * @author shizhongming
 * 2025/2/8 22:10
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseMessageFileEvent extends AbstractChatCompletionResponse {

    /**
     * 文件ID
     */
    private String id;

    /**
     * 文件类型
     */
    @JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
    private ChatFileTypeEnum type;

    /**
     * 文件归属，user或assistant，该接口返回仅为 assistant
     */
    @JsonProperty("belongs_to")
    private String belongsTo;

    /**
     * 文件访问地址
     */
    private String url;
}
