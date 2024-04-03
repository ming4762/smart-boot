package com.smart.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.annotation.TableUseYnField;
import com.smart.crud.model.BaseModel;
import com.smart.crud.plus.logic.TableLogicKey;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_tenant_package - 租户产品套餐
* @author SmartCodeGenerator
* 2024年4月2日 下午3:02:14
*/
@Getter
@Setter
@TableName("sys_tenant_package")
public class SysTenantPackagePO extends BaseModel {

    @Serial
    private static final long serialVersionUID = -227243760091329362L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * package_code - 产品包编码
    */
    private String packageCode;

    /**
    * package_name - 产品包名
    */
    private String packageName;

    /**
    * effect_time - 生效时间
    */
    private LocalDateTime effectTime;

    /**
    * expire_time - 过期时间
    */
    private LocalDateTime expireTime;

    /**
    * remark - 备注
    */
    private String remark;

    /**
    * seq - 序号
    */
    private Integer seq;

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
    @TableLogicKey
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