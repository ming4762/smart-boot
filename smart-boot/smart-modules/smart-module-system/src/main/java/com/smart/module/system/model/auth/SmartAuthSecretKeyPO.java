package com.smart.module.system.model.auth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.framework.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
* smart_auth_secret_key - 秘钥管理
* @author SmartCodeGenerator
* 2023-2-19
*/
@Getter
@Setter
@TableName("smart_auth_secret_key")
public class SmartAuthSecretKeyPO extends BaseModelUserTime {

    @Serial
    private static final long serialVersionUID = 4913192683077338409L;

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * key_name - 秘钥名称
    */
    private String keyName;

    private Long systemId;

    /**
    * file_storage_id - 文件存储器ID
    */
    private Long fileStorageId;

    /**
    * seq - seq
    */
    private Integer seq;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String storePassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String keyPassword;

    /**
    * alias - 私钥别称
    */
    private String alias;

    private Long publicKeyFileId;

    private Long privateKeyFileId;

    /**
    * remark - remark
    */
    private String remark;

    private Boolean useYn;

    @TableLogic
    private Boolean deleteYn;

}