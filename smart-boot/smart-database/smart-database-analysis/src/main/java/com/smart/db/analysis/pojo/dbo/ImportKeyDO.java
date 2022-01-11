package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2020/7/25 17:11
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class ImportKeyDO extends AbstractDatabaseBaseDO {

    private static final long serialVersionUID = 2244980886860996732L;

    @DatabaseField("PKTABLE_CAT")
    private String pktableCat;

    @DatabaseField("PKTABLE_SCHEM")
    private String pktableSchem;

    @DatabaseField("PKTABLE_NAME")
    private String pktableName;

    @DatabaseField("PKCOLUMN_NAME")
    private String pkcolumnName;

    @DatabaseField("FKTABLE_CAT")
    private String fktableCat;

    @DatabaseField("FKTABLE_SCHEM")
    private String fktableSchem;

    @DatabaseField("FKTABLE_NAME")
    private String fktableName;

    @DatabaseField("FKCOLUMN_NAME")
    private String fkcolumnName;

    @DatabaseField("KEY_SEQ")
    private Integer keySeq;

    @DatabaseField("UPDATE_RULE")
    private String updateRule;

    @DatabaseField("DELETE_RULE")
    private Integer deleteRule;

    @DatabaseField("FK_NAME")
    private String fkName;

    @DatabaseField("PK_NAME")
    private String pkName;

    @DatabaseField("DEFERRABILITY")
    private Integer deferrability;
}
