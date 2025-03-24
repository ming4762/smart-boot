package com.smart.module.code.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModel;
import com.smart.module.code.constants.ButtonIdentEnum;
import com.smart.module.code.type.ButtonIdentTypeHandler;
import lombok.*;

import java.io.Serial;

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
    @Serial
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
    @TableField(typeHandler = ButtonIdentTypeHandler.class)
    private ButtonIdentEnum ident;

    private String button;

    private Integer seq;
}
