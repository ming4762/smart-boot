package com.smart.framework.tool.database.pojo.dbo;

import com.smart.framework.tool.database.annotation.DatabaseField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * table view DO类
 * @author ShiZhongMing
 * 2020/7/25 16:30
 * @since 1.0
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class TableViewDO extends AbstractTableBaseDO {

    @Serial
    private static final long serialVersionUID = 8022824330680317557L;

    @DatabaseField("TABLE_TYPE")
    private String tableType;

    @DatabaseField("REMARKS")
    private String remarks;

    @DatabaseField("TYPE_CAT")
    private String typeCat;

    @DatabaseField("TYPE_SCHEM")
    private String typeSchem;

    @DatabaseField("TYPE_NAME")
    private String typeName;

    @DatabaseField("SELF_REFERENCING_COL_NAME")
    private String selfReferencingColName;

    @DatabaseField("REF_GENERATION")
    private String refGeneration;
}
