package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 获取会话列表响应
 * @author shizhongming
 * 2025/2/14 20:35
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyConversationListResponse {

    private List<ConversationItem> data;

    @JsonProperty("has_more")
    private Boolean hasMore;

    /**
     * 返回条数，若传入超过系统限制，返回系统限制数量
     */
    private Long limit;

    @Getter
    @Setter
    public static class ConversationItem {

        /**
         * 会话 ID
         */
        private String id;

        /**
         * 会话名称，默认为会话中用户最开始问题的截取。
         */
        private String name;

        /**
         * 用户输入参数。
         */
        private Map<String, Object> inputs;

        /**
         * 会话状态。
         */
        private String status;

        /**
         * 开场白。
         */
        private String introduction;

        /**
         * 会话创建时间。
         */
        @JsonProperty("created_at")
        private Long createdAt;

        /**
         * 会话更新时间。
         */
        @JsonProperty("updated_at")
        private Long updatedAt;
    }
}
