package com.smart.module.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.model.BaseModelUserTime;
import com.smart.module.system.constants.SysConfigStorageIdentEnum;
import lombok.*;

/**
* sys_config_storage - 配置存储表
* @author SmartCodeGenerator
* 2024年12月31日 11:27:39
*/
@Getter
@Setter
@TableName("sys_config_storage")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysConfigStoragePO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * config_identifier - 配置标识 UI_VXE_CUSTOM：vxe custom存储
    */
    private SysConfigStorageIdentEnum configIdentifier;

    /**
    * config_key - 配置key
    */
    private String configKey;

    /**
    * config_value - 配置value
    */
    private String configValue;

    /**
    * belong_user_id - 所属用户ID
    */
    private Long belongUserId;

    /**
    * tenant_id - 租户ID
    */
    private Long tenantId;
}