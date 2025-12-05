package com.smart.framework.tool.database.pojo.dbo;

import com.smart.framework.tool.database.annotation.DatabaseField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 字段注释
 * @author ShiZhongMing
 * 2020/7/25 17:11
 * @since 1.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ColumnRemarkDO extends AbstractDatabaseBaseDO {

    @Serial
    private static final long serialVersionUID = 374924028298986492L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField("REMARK")
    private String remark;
}
