package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传结果
 * @author shizhongming
 * 2025/2/10 21:37
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyFileUploadResponse {

    private String id;

    private String name;

    private Long size;

    private String extension;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_at")
    private Long createdAt;
}
