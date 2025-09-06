package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* sys_parameter - 系统参数表
* @author SmartCodeGenerator
* 2023-2-27
*/
@Getter
@Setter
@TableName("sys_parameter")
public class SysParameterPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = -8602107933127602522L;

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
    @TableUseYnField
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}