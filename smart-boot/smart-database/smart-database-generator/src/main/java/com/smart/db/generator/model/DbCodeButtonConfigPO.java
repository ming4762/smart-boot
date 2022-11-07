package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import com.smart.db.generator.constants.ButtonIdentEnum;
import lombok.*;

/**
 * @author shizhongming
 * 2021/5/11 9:21 下午
 */
@Getter
@Setter
@TableName(value = "db_code_button_config", autoResultMap = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DbCodeButtonConfigPO extends BaseModel {
    private static final long serialVersionUID = -5227167613945536367L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联ID
     */
    private Long relatedId;

    /**
     * 按钮标识
     */
    private ButtonIdentEnum ident;

    private String button;

    private Integer seq;
}
