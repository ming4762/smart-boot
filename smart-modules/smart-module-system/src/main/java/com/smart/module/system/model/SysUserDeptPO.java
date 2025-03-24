package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.model.BaseModelUserTime;
import com.smart.module.system.constants.UserDeptIdentEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2022/10/17
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_dept")
public class SysUserDeptPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = 1882267700344552707L;

    @TableId
    private Long userId;

    private Long deptId;

    private UserDeptIdentEnum ident;

    private String dataScope;

    @TableTenantField
    private Long tenantId;
}
