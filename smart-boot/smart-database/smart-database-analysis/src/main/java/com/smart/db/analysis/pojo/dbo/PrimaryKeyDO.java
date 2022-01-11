package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 主键信息
 * @author ShiZhongMing
 * 2020/7/25 17:09
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class PrimaryKeyDO extends AbstractTableBaseDO {
    private static final long serialVersionUID = -6872572473248995771L;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    /**
     * 主键序号
     */
    @DatabaseField("KEY_SEQ")
    private Integer keySeq;

    /**
     * 主键名称
     */
    @DatabaseField("PK_NAME")
    private String pkName;
}
