package com.smart.commons.core.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/9/9
 * @since 1.0
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
public class DocumentVO {

    private String name;

    private String remark;

    private String type;

    /**
     * 可选值
     */
    private List<String> optional;

    /**
     * 默认值
     */
    private String defaultValue;

    private Boolean nullable;

    private List<DocumentVO> fieldList;

    public DocumentVO(String name, String remark, String type, List<String> optional, String defaultValue, Boolean nullable) {
        this.name = name;
        this.remark = remark;
        this.type = type;
        this.optional = optional;
        this.defaultValue = defaultValue;
        this.nullable = nullable;
    }
}
