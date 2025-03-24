package com.smart.framework.tool.database.pojo.dbo;

import com.smart.framework.tool.database.annotation.DatabaseField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * 表格信息基础类
 * @author ShiZhongMing
 * 2020/7/25 17:05
 * @since 0.0.6
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AbstractTableBaseDO extends AbstractDatabaseBaseDO {
    @Serial
    private static final long serialVersionUID = -3056893832662371382L;

    @DatabaseField("TABLE_CAT")
    private String tableCat;

    @DatabaseField("TABLE_SCHEM")
    private String tableSchem;

    @DatabaseField("TABLE_NAME")
    private String tableName;
}
