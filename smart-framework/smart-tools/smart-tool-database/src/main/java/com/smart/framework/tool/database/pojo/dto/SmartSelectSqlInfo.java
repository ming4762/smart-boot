package com.smart.framework.tool.database.pojo.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.sf.jsqlparser.statement.select.Select;

import java.util.List;

/**
 * 查询语句解析结果
 * @author shizhongming
 * 2025/8/13 17:02
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class SmartSelectSqlInfo extends SmartSqlInfo {

    /**
     * 是否包含*列
     */
    private boolean hasStarColumn;

    /**
     * 列信息
     */
    private List<SmartSelectColumn> columnList;

    /**
     * 元数据列信息
     */
    private List<SmartSelectMetaDataColumn> metaDataColumnList;

    /**
     * where 条件
     */
    private List<SmartSelectWhere> whereList;

    /**
     * 解析后的查询语句
     */
    public Select getSelect() {
        return (Select) getStatement();
    }
}
