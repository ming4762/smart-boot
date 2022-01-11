package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2021/6/16 8:14
 * @since 1.0
 */
@Getter
@Setter
@TableName("db_code_template_user_group")
@AllArgsConstructor
@NoArgsConstructor
public class DbCodeTemplateUserGroupPO extends BaseModelCreateUserTime {
    private static final long serialVersionUID = -6618377670379125812L;

    @TableId(type = IdType.NONE)
    private Long templateId;

    private Long groupId;

}
