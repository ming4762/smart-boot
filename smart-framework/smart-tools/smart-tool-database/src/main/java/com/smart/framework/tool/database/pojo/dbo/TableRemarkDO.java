package com.smart.framework.tool.database.pojo.dbo;

import com.smart.framework.tool.database.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

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
    @Serial
    private static final long serialVersionUID = 3904911515668284196L;

    @DatabaseField("TABLE_NAME")
    private String tableName;

    @DatabaseField("REMARK")
    private String remark;
}
