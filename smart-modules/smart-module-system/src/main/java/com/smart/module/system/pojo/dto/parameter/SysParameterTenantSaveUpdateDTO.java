package com.smart.module.system.pojo.dto.parameter;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_parameter_tenant - 系统参数租户表
* @author SmartCodeGenerator
* 2025年9月4日 19:47:33
*/
@Getter
@Setter
@ToString
public class SysParameterTenantSaveUpdateDTO implements Serializable {

    /**
    * id
    */
    private Long id;

    @NotBlank(message = "参数ID不能为空")
    private Long parameterId;

    /**
    * 参数值
    */
    @NotBlank(message = "参数值不能为空")
    private String parameter;

    private Boolean commonYn;
}