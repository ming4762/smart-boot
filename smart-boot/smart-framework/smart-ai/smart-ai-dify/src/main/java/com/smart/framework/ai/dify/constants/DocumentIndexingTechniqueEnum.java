package com.smart.framework.ai.dify.constants;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.Getter;

/**
 * 索引方式
 * @author shizhongming
 * 2025/2/15 15:40
 * @since 5.0.0
 */
@Getter
@JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
public enum DocumentIndexingTechniqueEnum implements EnumValue {

    /**
     * 索引方式
     */
    HIGH_QUALITY("high_quality", "高质量：使用 embedding 模型进行嵌入，构建为向量数据库索引"),
    ECONOMY("economy", "经济：使用 keyword table index 的倒排索引进行构建"),
    ;

    private final String value;
    private final String description;

    DocumentIndexingTechniqueEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
