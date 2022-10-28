package com.smart.system.model;

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
public class SysRoleRelationPO extends BaseModelCreateUserTime {
    @Serial
    private static final long serialVersionUID = -3101700604215505813L;

    private Long roleId;

    private Long relatedRoleId;

    private RoleRelationTypeEnum relationType;

    private String relationPath;
}
