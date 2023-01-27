package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.db.generator.constants.RowButtonTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:32
 * @since 1.0
 */
@Getter
@Setter
@TableName("db_code_main")
public class DbCodeMainPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = 3775353499239687449L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long connectionId;

    /**
     * 所属系统ID
     */
    private Long systemId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 类名
     */
    private String className;

    private String tableName;

    private String type;

    private Boolean showCheckbox;

    /**
     * 是否分页
     */
    private Boolean page;

    /**
     * 虚拟表格
     */
    private Boolean invented;

    private Integer formColNum;

    private Integer searchColNum;

    private String remark;

    private String i18nPrefix;

    /**
     * 表备注
     */
    @TableField("table_remark")
    private String remarks;

    /**
     * 列顺序是否可调
     */
    private Boolean columnSort;

    /**
     * 行按钮类型
     */
    private RowButtonTypeEnum rowButtonType;
}
