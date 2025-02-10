package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Agent模式下有关Agent思考步骤的相关内容，涉及到工具调用（仅Agent模式下使用）
 * @author shizhongming
 * 2025/2/8 22:11
 * @since 5.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponseAgentThoughtEvent extends AbstractChatCompletionResponse {

    /**
     * agent_thought ID，每一轮Agent迭代都会有一个唯一的id
     */
    private String id;

    /**
     * agent_thought在消息中的位置，如第一轮迭代position为1
     */
    private Long position;

    /**
     * agent的思考内容
     */
    private String thought;

    /**
     * 工具调用的返回结果
     */
    private String observation;

    /**
     * (string) 使用的工具列表，以 ; 分割多个工具
     */
    private String tool;

    /**
     * 工具的输入，JSON格式的字符串(object)。如：{"dalle3": {"prompt": "a cute cat"}}
     */
    @JsonProperty("tool_input")
    private String toolInput;

    /**
     *  当前 agent_thought 关联的文件ID
     */
    @JsonProperty("message_files")
    private List<String> messageFiles;

    /**
     * 消息创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
