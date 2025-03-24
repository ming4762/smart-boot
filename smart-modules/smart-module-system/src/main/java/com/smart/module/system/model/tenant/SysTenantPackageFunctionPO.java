package com.smart.module.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelCreateUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* sys_tenant_package_function - 租户套餐-功能菜单关联关系表
* @author SmartCodeGenerator
* 2024年4月3日 下午1:40:45
*/
@Getter
@Setter
@TableName("sys_tenant_package_function")
public class SysTenantPackageFunctionPO extends BaseModelCreateUserTime {

    @Serial
    private static final long serialVersionUID = 7661133374867750242L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * tenant_package_id - tenantPackageId
    */
    private Long tenantPackageId;

    /**
    * function_id - functionId
    */
    private Long functionId;

    /**
    * half_yn - 是否半选中
    */
    private Boolean halfYn;

}