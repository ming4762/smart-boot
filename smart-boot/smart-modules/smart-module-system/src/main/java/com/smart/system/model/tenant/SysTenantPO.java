package com.smart.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
* sys_tenant - 租户表
* @author SmartCodeGenerator
* 2023-2-26 12:18:21
*/
@Getter
@Setter
@TableName("sys_tenant")
public class SysTenantPO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * tenant_code - 租户编号
    */
    private String tenantCode;

    /**
    * tenant_name - 租户名字
    */
    private String tenantName;

    /**
    * contacts - 联系人
    */
    private String contacts;

    /**
    * contact_phone - 联系人电话
    */
    private String contactPhone;

    /**
    * domain - 域名
    */
    private String domain;

    /**
    * available_user_num - 可用人数，-1不限制
    */
    private Long availableUserNum;

    /**
    * address - 地址
    */
    private String address;

    /**
    * logo_id - LOGO
    */
    private Long logoId;

    /**
    * start_time - 开始时间
    */
    private LocalDateTime startTime;

    /**
    * end_time - 过期时间
    */
    private LocalDateTime endTime;

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
    private Boolean useYn;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

}