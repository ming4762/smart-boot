package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:39
 * @since 1.0
 */
@Getter
@Setter
@TableName("db_code_page_config")
public class DbCodePageConfigPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = 4336964239066778547L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long mainId;

    private String columnName;

    private String javaType;

    private String javaProperty;

    private String simpleJavaType;

    private String typeName;

    private Integer columnSize;

    private Integer decimalDigits;

    private String columnDef;

    private Boolean nullable;

    private String remarks;

    private Boolean primaryKey;

    private Boolean indexed;

    private String tableName;

    private String extType;

    private String title;

    private Boolean sortable;

    private String fixed;

    private String width;

    private String align;

    private Boolean resizable;

    private Boolean visible;

    private Boolean hidden;

    private String format;

    private Integer seq;

    /**
     * 是否可编辑
     */
    private Boolean editable;
}
