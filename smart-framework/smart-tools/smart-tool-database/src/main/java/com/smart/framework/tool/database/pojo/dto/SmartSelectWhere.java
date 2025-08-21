package com.smart.framework.tool.database.pojo.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 查询条件
 * @author shizhongming
 * 2025/8/14 09:00
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SmartSelectWhere implements Serializable {

    /**
     * 列名
     */
    private String columnFull;

    private String columnName;

    /**
     * 操作符
     */
    private String operator;

    /**
     * 值
     */
    private String value;

    /**
     * 是否使用占位符
     */
    private boolean placeholder;


}
