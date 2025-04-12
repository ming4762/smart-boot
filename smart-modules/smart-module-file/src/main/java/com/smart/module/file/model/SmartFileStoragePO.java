package com.smart.module.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* smart_file_storage - 文件存储器配置
* @author SmartCodeGenerator
* 2023-2-14
*/
@Getter
@Setter
@TableName("smart_file_storage")
public class SmartFileStoragePO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 6375283259164374950L;
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
     * 文件是否加密
     */
    private Boolean encryptedYn;

    /**
    * encrypt_key - 加密秘钥
    */
    private String publicKey;

    /**
    * encrypt_key - 加密秘钥
    */
    private String privateKey;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    @TableLogic
    private Boolean deleteYn;

}