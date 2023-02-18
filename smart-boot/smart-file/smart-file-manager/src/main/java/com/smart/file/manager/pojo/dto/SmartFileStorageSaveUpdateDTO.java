package com.smart.file.manager.pojo.dto;

import com.smart.file.core.constants.FileStorageTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* smart_file_storage - 文件存储器配置
* @author SmartCodeGenerator
* 2023-2-14 15:19:53
*/
@Getter
@Setter
@ToString
public class SmartFileStorageSaveUpdateDTO implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 存储器编码
    */
    private String storageCode;
    /**
    * 存储器名称
    */
    private String storageName;
    /**
    * 存储器类型
    */
    private FileStorageTypeEnum storageType;
    /**
    * 是否是默认存储器
    */
    private Boolean defaultStorage;
    /**
    * 
    */
    private Integer seq;
    /**
    * 
    */
    private Boolean useYn;
    /**
    * 
    */
    private String remark;
    /**
    * 存储器配置信息
    */
    private String storageConfig;

}