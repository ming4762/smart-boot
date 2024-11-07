package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.*;

import java.io.Serial;

/**
 * @author jackson
 * 2020/1/24 2:18 下午
 */
@TableName("sys_role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysRolePO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 6200571838296972907L;
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 是否是超级管理员角色
     */
    private Boolean superAdminYn;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean useYn;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 租户ID
     */
    @TableTenantField
    private Long tenantId;
}
