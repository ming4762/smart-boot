package com.smart.file.manager.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.file.core.constants.FileStorageTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
* smart_file_storage - 文件存储器配置
* @author SmartCodeGenerator
* 2023-2-14
*/
@Getter
@Setter
@TableName("smart_file_storage")
public class SmartFileStoragePO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
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
    * seq - seq
    */
    private Integer seq;

    /**
    * remark - remark
    */
    private String remark;

    /**
    * default_storage - 是否是默认存储器
    */
    private Boolean defaultStorage;

    /**
    * storage_config - 存储器配置信息
    */
    private String storageConfig;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}