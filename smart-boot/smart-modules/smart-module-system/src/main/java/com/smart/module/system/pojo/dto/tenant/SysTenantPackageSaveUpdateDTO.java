package com.smart.module.system.pojo.dto.tenant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* sys_tenant_package - 租户产品套餐
* @author SmartCodeGenerator
* 2024年4月2日 下午3:02:14
*/
@Getter
@Setter
@ToString
public class SysTenantPackageSaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -4763651519163856492L;
    /**
    * id
    */
    private Long id;
    /**
    * 产品包编码
    */
    private String packageCode;
    /**
    * 产品包名
    */
    private String packageName;
    /**
    * 生效时间
    */
    private LocalDateTime effectTime;
    /**
    * 过期时间
    */
    private LocalDateTime expireTime;
    /**
    * 备注
    */
    private String remark;
    /**
    * 序号
    */
    private Integer seq;

}