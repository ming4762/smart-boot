package com.smart.module.system.pojo.dto.i18n;

import com.smart.module.system.constants.SysI18nPlatformEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_i18n_json - 国际化信息json
* @author SmartCodeGenerator
* 2025年1月3日 14:39:00
*/
@Getter
@Setter
@ToString
public class SysI18nJsonSaveUpdateDTO implements Serializable {

    /**
    * id
    */
    private Long id;
    /**
    * 平台
    */
    private SysI18nPlatformEnum platform;
    /**
    * 名称
    */
    private String name;
    /**
    * 备注
    */
    private String remark;
    /**
    * seq
    */
    private Integer seq;

}