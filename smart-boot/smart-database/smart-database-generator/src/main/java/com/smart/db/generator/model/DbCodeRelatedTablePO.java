package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import com.smart.db.generator.constants.RelatedTableIdentEnum;
import com.smart.db.generator.constants.RelatedTableTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2021/5/12 9:16 下午
 */
@Setter
@Getter
@TableName("db_code_related_table")
public class DbCodeRelatedTablePO extends BaseModel {
    @Serial
    private static final long serialVersionUID = -4522273528733971546L;

    /**
     * 主ID
     */
    @TableId
    private Long mainId;

    /**
     * 附表ID
     */
    private Long addendumId;

    private RelatedTableIdentEnum ident;

    /**
     * 关联字段
     */
    private String relatedColumn;

    /**
     * 类型
     */
    private RelatedTableTypeEnum type;

    /**
     * 序号
     */
    private Integer seq;
}
