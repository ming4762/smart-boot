package com.smart.system.pojo.dto.parameter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_parameter - 系统参数表
* @author SmartCodeGenerator
* 2023-2-27
*/
@Getter
@Setter
@ToString
public class SysParameterSaveUpdateDTO implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 参数编码
    */
    private String code;
    /**
    * 参数名字
    */
    private String name;
    /**
    * 参数值
    */
    private String parameter;
    /**
    * 备注
    */
    private String remark;
    /**
    * 
    */
    private Integer seq;
    /**
    * 
    */
    private Boolean useYn;

}