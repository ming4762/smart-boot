package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 代码模板分组实体
 * @author ShiZhongMing
 * 2022/1/27
 * @since 2.0.0
 */
@Getter
@Setter
@TableName("db_code_template_group")
public class DbCodeTemplateGroupPO extends BaseModelUserTime {

    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;

    private String groupName;

    private Integer seq;
}
