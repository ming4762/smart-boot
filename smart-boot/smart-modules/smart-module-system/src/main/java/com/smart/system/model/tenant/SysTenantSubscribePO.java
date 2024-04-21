package com.smart.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.annotation.TableLogicField;
import com.smart.crud.annotation.TableUseYnField;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_tenant_subscribe - 租户套餐订阅表
* @author SmartCodeGenerator
* 2024年4月6日 下午6:41:30
*/
@Getter
@Setter
@TableName("sys_tenant_subscribe")
public class SysTenantSubscribePO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 4811432901187781308L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * tenant_id - 租户ID
    */
    private Long tenantId;

    /**
    * package_id - 套餐包ID
    */
    private Long packageId;

    /**
    * effect_time - 生效日期
    */
    private LocalDateTime effectTime;

    /**
    * expire_time - 失效日期
    */
    private LocalDateTime expireTime;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * user_number - 用户数
    */
    private Long userNumber;

    /**
    * use_yn - useYn
    */
    @TableUseYnField
    private Boolean useYn;

    /**
    * create_time - createTime
    */
    private LocalDateTime createTime;

    /**
    * create_user_id - createUserId
    */
    private Long createUserId;

    /**
    * create_by - createBy
    */
    private String createBy;

    /**
    * update_time - updateTime
    */
    private LocalDateTime updateTime;

    /**
    * update_user_id - updateUserId
    */
    private Long updateUserId;

    /**
    * update_by - updateBy
    */
    private String updateBy;

    /**
    * delete_yn - deleteYn
    */
    @TableLogic
    private Boolean deleteYn;

    /**
    * delete_key - deleteKey
    */
    @TableLogicField(isDeleteKey = true)
    private Long deleteKey;

    /**
    * delete_time - deleteTime
    */
    private LocalDateTime deleteTime;

    /**
    * delete_by - deleteBy
    */
    private String deleteBy;

    /**
    * delete_user_id - deleteUserId
    */
    private Long deleteUserId;

}