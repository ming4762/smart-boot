package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_parameter_tenant - 系统参数租户表
* @author SmartCodeGenerator
* 2025年9月4日 19:47:33
*/
@Getter
@Setter
@TableName("sys_parameter_tenant")
public class SysParameterTenantPO extends BaseModelUserTime {

    /**
    * 系统默认参数租户id
    */
    public static final Long COMMON_PARAMETER_TENANT_ID = -1L;

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * parameter_id - parameterId
    */
    private Long parameterId;

    /**
    * tenant_id - 租户id, -1则是通用值
    */
    private Long tenantId;

    /**
    * parameter - 参数值
    */
    private String parameter;
}