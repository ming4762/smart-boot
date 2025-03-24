package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 获取会话历史消息
 * @author shizhongming
 * 2025/2/10 21:03
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyMessageHistoryResponse {

    /**
     * 消息ID
     */
    private String id;

    /**
     * 会话 ID
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     *  用户输入参数
     */
    private Map<String, Object> inputs;

    /**
     * 用户输入 / 提问内容。
     */
    private String query;

    /**
     * 消息文件
     */
    @JsonProperty("message_files")
    private List<ChatResponseMessageFileEvent> messageFiles;

    /**
     * Agent思考内容（仅Agent模式下不为空）
     */
    @JsonProperty("agent_thoughts")
    private List<ChatResponseAgentThoughtEvent> agentThoughts;

    /**
     * 回答消息内容
     */
    private String answer;

    /**
     * 消息创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;

    /**
     * 反馈信息
     */
    private Feedback feedback;

    @JsonProperty("retriever_resources")
    private List<Object> retrieverResources;

    /**
     * 是否存在下一页
     */
    @JsonProperty("has_more")
    private Boolean hasMore;

    /**
     * 返回条数，若传入超过系统限制，返回系统限制数量
     */
    private Integer limit;


    @Getter
    @Setter
    public static class Feedback {
        /**
         * (string) 点赞 like / 点踩 dislike
         */
        private String rating;
    }
}
