package com.smart.module.system.pojo.dto.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* sys_config_storage - 配置存储表
* @author SmartCodeGenerator
* 2024年12月31日 11:27:39
*/
@Getter
@Setter
@ToString
public class SysConfigStorageSaveUpdateDTO implements Serializable {

    /**
    * 配置key
    */
    private String configKey;
    /**
    * 配置value
    */
    private String configValue;
}