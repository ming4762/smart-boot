package com.smart.system.pojo.dto.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* sys_system - 系统管理表
* @author SmartCodeGenerator
* 2023-1-22 20:00:47
*/
@Getter
@Setter
@ToString
public class SysSystemSaveUpdateDTO implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 系统编码
    */
    @NotNull(message = "请输入系统编码")
    private String code;
    /**
    * 系统名称
    */
    @NotNull(message = "请输入系统名称")
    private String name;
    /**
    * 备注
    */
    private String remark;
    /**
    * 所属公司
    */
    private String enterprise;
    /**
    * 系统版本
    */
    private String version;

    private Integer seq;

}