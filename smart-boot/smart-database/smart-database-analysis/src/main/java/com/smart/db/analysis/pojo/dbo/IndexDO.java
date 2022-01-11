package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 索引信息
 * @author ShiZhongMing
 * 2020/7/25 17:08
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class IndexDO extends AbstractTableBaseDO {
    private static final long serialVersionUID = -1195053644810330715L;

    @DatabaseField("NON_UNIQUE")
    private Integer nonUnique;

    @DatabaseField("INDEX_QUALIFIER")
    private String indexQualifier;

    @DatabaseField("INDEX_NAME")
    private String indexName;

    @DatabaseField("TYPE")
    private Integer type;

    @DatabaseField("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("ASC_OR_DESC")
    private String ascOrDesc;

    @DatabaseField("CARDINALITY")
    private Integer cardinality;

    @DatabaseField("PAGES")
    private Integer pages;

    @DatabaseField("FILTER_CONDITION")
    private String filterCondition;
}
