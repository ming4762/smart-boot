package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字段注释
 * @author ShiZhongMing
 * 2020/7/25 17:11
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ColumnRemarkDO extends AbstractDatabaseBaseDO {

    private static final long serialVersionUID = 374924028298986492L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("REMARK")
    private String remark;
}
