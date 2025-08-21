package com.smart.framework.tool.database.pojo.dto;

import com.smart.framework.tool.database.constants.TypeMappingEnum;
import lombok.*;

import java.io.Serializable;

/**
 * SQL查询元数据列
 * @author shizhongming
 * 2025/8/20 09:07
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartSelectMetaDataColumn implements Serializable {

    private String tableName;


    private String label;

    private String columnName;

    private TypeMappingEnum columnType;

    private String columnTypeName;

    private int precision;

    private int scale;

    private boolean nullable;
}
