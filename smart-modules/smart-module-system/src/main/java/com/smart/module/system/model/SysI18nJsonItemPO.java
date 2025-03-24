package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_i18n_json_item - 国际化信息json项
* @author SmartCodeGenerator
* 2025年1月3日 19:22:50
*/
@Getter
@Setter
@TableName("sys_i18n_json_item")
public class SysI18nJsonItemPO extends BaseModelDeleteUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * head_id - headId
    */
    private Long headId;

    /**
    * locale - 语言
    */
    private String locale;

    /**
    * data - 数据
    */
    private String data;

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