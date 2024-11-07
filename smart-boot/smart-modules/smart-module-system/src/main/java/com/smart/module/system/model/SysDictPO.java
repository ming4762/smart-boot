package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableTenantField;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.SqlCommandType;

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

    /**
     * 查询逻辑单独写
     */
    @TableTenantField(ignoreCommands = SqlCommandType.SELECT, platformTenantIgnoreCommands = SqlCommandType.INSERT)
    private Long tenantId;

    /**
     * 是否平台通用
     */
    private Boolean tenantCommonYn;
}

