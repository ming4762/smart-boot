package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * 索引内容的形式
 * @author shizhongming
 * 2025/2/15 15:42
 * @since 5.0.0
 */
@Getter
public enum DocumentFormEnum implements EnumValue {
    /**
     * 索引内容的形式
     */
    TEXT_MODEL("text_model", "text 文档直接 embedding，经济模式默认为该模式"),
    HIERARCHICAL_MODEL("hierarchical_model", "parent-child 模式"),
    QA_MODEL("qa_model", "Q&A 模式：为分片文档生成 Q&A 对，然后对问题进行 embedding"),
    ;

    private final String value;
    private final String description;

    DocumentFormEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
