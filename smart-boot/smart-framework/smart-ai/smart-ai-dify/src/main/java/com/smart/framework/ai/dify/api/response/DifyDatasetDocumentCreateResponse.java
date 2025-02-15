package com.smart.framework.ai.dify.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 创建文档响应
 * @author shizhongming
 * 2025/2/15 15:49
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyDatasetDocumentCreateResponse {

    private Map<String, Object> document;

    private String batch;
}
