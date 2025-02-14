package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 会话重命名响应
 * @author shizhongming
 * 2025/2/15 1:05
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyConversationRenameResponse {

    /**
     * 会话 ID
     */
    private String id;

    /**
     * 会话名称
     */
    private String name;

    /**
     * 用户输入参数
     */
    private Map<String, Object> inputs;

    /**
     *  会话状态
     */
    private String status;

    /**
     * 开场白
     */
    private String introduction;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;
}
