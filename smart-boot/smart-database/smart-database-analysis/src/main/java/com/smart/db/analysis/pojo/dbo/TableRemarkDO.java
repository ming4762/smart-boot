package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 表注释
 * @author ShiZhongMing
 * 2020/7/25 17:10
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class TableRemarkDO extends AbstractDatabaseBaseDO {
    private static final long serialVersionUID = 3904911515668284196L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("REMARK")
    private String remark;
}
