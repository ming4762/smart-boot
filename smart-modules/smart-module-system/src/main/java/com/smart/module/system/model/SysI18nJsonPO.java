package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import com.smart.module.system.constants.SysI18nPlatformEnum;
import lombok.Getter;
import lombok.Setter;

/**
* sys_i18n_json - 国际化信息json
* @author SmartCodeGenerator
* 2025年1月3日 14:39:00
*/
@Getter
@Setter
@TableName("sys_i18n_json")
public class SysI18nJsonPO extends BaseModelDeleteUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * platform - 平台
    */
    private SysI18nPlatformEnum platform;

    /**
    * name - 名称
    */
    private String name;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * use_yn - useYn
    */
    @TableUseYnField
    private Boolean useYn;

    /**
    * seq - seq
    */
    private Integer seq;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;
}