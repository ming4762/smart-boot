package com.smart.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_tenant_user - 租户用户关联关系表
* @author SmartCodeGenerator
* 2024年4月6日 下午8:25:53
*/
@Getter
@Setter
@TableName("sys_tenant_user")
public class SysTenantUserPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 7320372452888494859L;
    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * tenant_id - tenantId
    */
    private Long tenantId;

    /**
    * user_id - userId
    */
    private Long userId;

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

}