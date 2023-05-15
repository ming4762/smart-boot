package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import com.smart.system.constants.RoleRelationTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 角色关系表
 * @author ShiZhongMing
 * 2022/10/28
 * @since 3.0.0
 */
@Getter
@Setter
@TableName("sys_role_relation")
public class SysRoleRelationPO extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = -3101700604215505813L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long roleId;

    private Long relatedRoleId;

    private RoleRelationTypeEnum relationType;

    private String relationPath;
}
