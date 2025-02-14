package com.smart.framework.ai.dify.constants;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;

/**
 * 点赞 like, 点踩 dislike
 * @author shizhongming
 * 2025/2/15 0:57
 * @since 5.0.0
 */
@Getter
@JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
public enum DifyFeedbackRatingEnum implements EnumValue {

    /**
     * 点赞 like, 点踩 dislike
     */
    LIKE("like"),

    DISLIKE("dislike");

    private final String value;
    DifyFeedbackRatingEnum(String value) {
        this.value = value;
    }
}
