package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* sys_dict - 系统字典表
* @author GCCodeGenerator
* 2022-1-29 10:34:36
*/
@Getter
@Setter
@TableName("sys_dict")
public class SysDictPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = -4129235312297061914L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * dict_code - 字典编码
    */
    private String dictCode;

    /**
    * dict_name - 字典名称
    */
    private String dictName;

    /**
    * seq - 序号
    */
    private Integer seq;

    private String remark;

    /**
    * use_yn - 启用状态
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}
