package com.smart.module.api.file.dto;

import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/3/21
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SmartFileStorageListDTO implements Serializable {

    private Long id;

    /**
     * storage_code - 存储器编码
     */
    private String storageCode;

    /**
     * storage_name - 存储器名称
     */
    private String storageName;

    /**
     * storage_type - 存储器类型
     */
    private FileStorageTypeEnum storageType;

    /**
     * default_storage - 是否是默认存储器
     */
    private Boolean defaultStorage;

    /**
     * storage_config - 存储器配置信息
     */
    private String storageConfig;
}
