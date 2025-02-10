package com.smart.framework.ai.dify.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 引用和归属分段列表
 * @author shizhongming
 * 2025/2/8 19:54
 * @since 5.0.0
 */
@Getter
@Setter
public class RetrieverResource {

    @JsonProperty("position")
    private Long position;

    @JsonProperty("dataset_id")
    private String datasetId;

    @JsonProperty("dataset_name")
    private String datasetName;

    @JsonProperty("document_id")
    private String documentId;

    @JsonProperty("document_name")
    private String documentName;

    @JsonProperty("segment_id")
    private String segmentId;

    @JsonProperty("score")
    private BigDecimal score;

    @JsonProperty("content")
    private String content;
}
