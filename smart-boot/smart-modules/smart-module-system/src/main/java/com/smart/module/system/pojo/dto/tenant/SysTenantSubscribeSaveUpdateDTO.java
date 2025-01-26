package com.smart.module.system.pojo.dto.tenant;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
* sys_tenant_subscribe - 租户套餐订阅表
* @author SmartCodeGenerator
* 2024年4月6日 下午6:41:30
*/
@Getter
@Setter
@ToString
public class SysTenantSubscribeSaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -7830035327769976736L;
    /**
    * id
    */
    private Long id;
    /**
     * 租户ID
     */
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    /**
    * 套餐包ID
    */
    @NotNull(message = "套餐ID不能为空")
    private Long packageId;
    /**
    * 生效日期
    */
    @NotNull(message = "生效日期不能为空")
    private ZonedDateTime effectTime;

    /**
    * 失效日期
    */
    @NotNull(message = "失效日期不能为空")
    private ZonedDateTime expireTime;
    /**
    * 用户数
    */
    @NotNull(message = "用户数不能为空")
    private Long userNumber;
    /**
    * 备注
    */
    private String remark;

}