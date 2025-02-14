package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * 会话重命名
 * @author shizhongming
 * 2025/2/15 1:03
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyConversationRenameRequest {

    @JsonIgnore
    @NonNull
    private String conversationId;

    /**
     * （选填）名称，若 auto_generate 为 true 时，该参数可不传。
     */
    private String name;

    /**
     * （选填）自动生成标题，默认 false。
     */
    @JsonProperty("auto_generate")
    private Boolean autoGenerate;

    /**
     * 用户标识，由开发者定义规则，需保证用户标识在应用内唯一。
     */
    @NonNull
    private String user;
}
