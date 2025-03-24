package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 角色数据权限关系表
 * @author shizhongming
 * 2025/3/11 10:49
 * @since 5.0.0
 */
@Getter
@Setter
@TableName("sys_role_data_permission")
public class SysRoleDataPermissionPO extends BaseModelCreateUserTime {

    @Serial
    private static final long serialVersionUID = 2643882482812768223L;
    
    @TableId(type = IdType.NONE)
    private Long roleId;

    private Long dataPermissionId;
    
}
