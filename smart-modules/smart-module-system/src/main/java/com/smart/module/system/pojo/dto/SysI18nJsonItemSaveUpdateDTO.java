package com.smart.module.system.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_i18n_json_item - 国际化信息json项
* @author SmartCodeGenerator
* 2025年1月3日 19:22:50
*/
@Getter
@Setter
@ToString
public class SysI18nJsonItemSaveUpdateDTO implements Serializable {

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
}