package com.smart.db.generator.model;

import com.smart.crud.model.BaseModelUserTime;
import com.smart.db.generator.constants.FromControlTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2021/5/9 3:38 下午
 */
@Getter
@Setter
public class DbCodeFormConfigCommonPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = -992374588735109553L;

    private Long mainId;

    private String columnName;

    private String remarks;

    private String title;

    private Boolean readonly;

    private Boolean hidden;

    /**
     * 控件类型
     */
    private FromControlTypeEnum controlType;

    private Integer seq;

    private Boolean useTableSearch;

    private String tableName;

    private String keyColumnName;

    private String valueColumnName;

    private String tableWhere;

    /**
     * 是否渲染
     */
    private Boolean visible;
    /**
     * 是否使用
     */
    private Boolean used;

    /**
     * java字段名
     */
    private String javaProperty;

    /**
     * ext类型
     */
    private String extType;

    /**
     * java类型
     */
    private String javaType;

    private String simpleJavaType;

    /**
     * 是否自动校验
     */
    private Boolean autoValidate;
}
