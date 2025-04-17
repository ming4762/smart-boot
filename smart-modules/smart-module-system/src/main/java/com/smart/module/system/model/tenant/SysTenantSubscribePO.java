package com.smart.module.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.ZonedDateTime;

/**
* sys_tenant_subscribe - 租户套餐订阅表
* @author SmartCodeGenerator
* 2024年4月6日 下午6:41:30
*/
@Getter
@Setter
@TableName("sys_tenant_subscribe")
public class SysTenantSubscribePO extends BaseModelDeleteUserTime {

    @Serial
    private static final long serialVersionUID = 4811432901187781308L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * tenant_id - 租户ID
    */
    private Long tenantId;

    /**
    * package_id - 套餐包ID
    */
    private Long packageId;

    /**
    * effect_time - 生效日期
    */
    private ZonedDateTime effectTime;

    /**
    * expire_time - 失效日期
    */
    private ZonedDateTime expireTime;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * user_number - 用户数
    */
    private Long userNumber;

    /**
    * use_yn - useYn
    */
    @TableUseYnField
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;


}