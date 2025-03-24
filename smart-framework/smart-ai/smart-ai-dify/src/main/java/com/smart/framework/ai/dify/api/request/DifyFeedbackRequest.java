package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.constants.DifyFeedbackRatingEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息反馈（点赞）
 * @author shizhongming
 * 2025/2/15 0:54
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyFeedbackRequest {

    /**
     * 消息 ID
     */
    @JsonProperty
    private String messageId;

    /**
     * 点赞 like, 点踩 dislike, 撤销点赞 null
     */
    private DifyFeedbackRatingEnum rating;

    /**
     * 用户标识，由开发者定义规则，需保证用户标识在应用内唯一。
     */
    private String user;

    /**
     * 消息反馈的具体信息。
     */
    private String content;
}
