package com.smart.module.code.pojo.vo;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * sql生成mapper 模板VO
 * @author shizhongming
 * 2025/8/21 14:06
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DbGenerateMapperBySqlTemplateVO implements Serializable {

    private String packageName;

    private String className;

    private String methodName;

    private String doClassName;

    private String sql;

    private List<Column> columnList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Column implements Serializable {
        private String columnName;

        private String javaProperty;

        private String simpleJavaType;

        private String typeName;
    }
}
