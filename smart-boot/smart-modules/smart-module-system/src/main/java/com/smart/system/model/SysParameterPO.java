package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_parameter - 系统参数表
* @author SmartCodeGenerator
* 2023-2-27
*/
@Getter
@Setter
@TableName("sys_parameter")
public class SysParameterPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * code - 参数编码
    */
    private String code;

    /**
    * name - 参数名字
    */
    private String name;

    /**
    * parameter - 参数值
    */
    private String parameter;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * build_in - 系统内置
    */
    private Boolean buildIn;

    /**
    * seq - seq
    */
    private Integer seq;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}