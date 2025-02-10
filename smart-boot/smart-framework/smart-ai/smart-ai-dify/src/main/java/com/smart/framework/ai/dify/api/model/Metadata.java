package com.smart.framework.ai.dify.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 元数据
 * @author shizhongming
 * 2025/2/8 21:31
 * @since 5.0.0
 */
@Getter
@Setter
public class Metadata {

    /**
     * 模型用量信息
     */
    private Usage usage;

    /**
     * 引用和归属分段列表
     */
    @JsonProperty("retriever_resources")
    private List<RetrieverResource> retrieverResources;
}
