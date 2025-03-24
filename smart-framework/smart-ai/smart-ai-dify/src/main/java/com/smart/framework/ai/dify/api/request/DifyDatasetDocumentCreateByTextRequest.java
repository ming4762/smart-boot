package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.ai.dify.constants.DocumentFormEnum;
import com.smart.framework.ai.dify.constants.DocumentIndexingTechniqueEnum;
import com.smart.framework.ai.dify.constants.DocumentTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * 通过文本创建文档
 * 此接口基于已存在知识库，在此知识库的基础上通过文本创建新的文档
 * @author shizhongming
 * 2025/2/15 15:37
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyDatasetDocumentCreateByTextRequest {

    /**
     * 知识库 ID
     */
    @JsonProperty("dataset_id")
    @NonNull
    private String datasetId;

    /**
     * 文档名称
     */
    @NonNull
    private String name;

    /**
     * 文档内容
     */
    @NonNull
    private String text;

    /**
     * 文档类型
     */
    @JsonProperty("doc_type")
    private DocumentTypeEnum docType;

    /**
     * 文档元数据（如提供文档类型则必填）。字段因文档类型而异
     */
    @JsonProperty("doc_metadata")
    private Object docMetadata;

    /**
     * 索引方式
     */
    @JsonProperty("indexing_technique")
    private DocumentIndexingTechniqueEnum indexingTechnique;

    /**
     * 索引内容的形式
     */
    @JsonProperty("doc_form")
    private DocumentFormEnum docForm;

    /**
     * 在 Q&A 模式下，指定文档的语言，例如：English、Chinese
     */
    @JsonProperty("doc_language")
    private String docLanguage;

    /**
     * 处理规则
     * TODO: 使用实体类
     */
    @JsonProperty("process_rule")
    private Object processRule;

    /**
     * 检索模式
     * TODO: 使用实体类
     */
    @JsonProperty("retrieval_model")
    private Object retrievalModel;

    /**
     * Embedding 模型名称
     */
    @JsonProperty("embedding_model")
    private String embeddingModel;

    /**
     * Embedding 模型供应商
     */
    @JsonProperty("embedding_model_provider")
    private String embeddingModelProvider;
}
