package com.smart.module.system.model.tenant;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.framework.crud.annotation.TableUseYnField;
import com.smart.framework.crud.model.BaseModelDeleteUserTime;
import com.smart.module.api.system.dto.SysTenantDTO;
import com.smart.module.system.constants.SysTenantIsolationStrategyEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.time.ZonedDateTime;

/**
* sys_tenant - 租户表
* @author SmartCodeGenerator
* 2024年3月29日 上午10:42:39
*/
@Getter
@Setter
@TableName("sys_tenant")
public class SysTenantPO extends BaseModelDeleteUserTime {

    @Serial
    private static final long serialVersionUID = 3718421432325722666L;
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
    * tenant_short_name - 简称
    */
    private String tenantShortName;

    /**
    * com.smart.framework.tool.code.type - 类型
    */
    private String type;

    /**
    * contacts - 联系人
    */
    private String contacts;

    /**
    * contact_phone - 联系人电话
    */
    private String contactPhone;

    /**
    * email - 邮箱
    */
    private String email;

    /**
    * isolation_strategy - 隔离策略
    */
    private SysTenantIsolationStrategyEnum isolationStrategy;

    /**
    * industry - 行业
    */
    private String industry;

    /**
    * domain - 域名
    */
    private String domain;

    /**
    * available _user_num - 可用人数
    */
    private Long availableUserNum;

    /**
    * region - 地区
    */
    private String region;

    /**
    * address - 地址
    */
    private String address;

    /**
    * logo_id - LOGO
    */
    private Long logoId;

    /**
    * effect_time - 生效时间
    */
    private ZonedDateTime effectTime;

    /**
    * expire_time - 过期时间
    */
    private ZonedDateTime expireTime;

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
     * 是否平台管理租户
     */
    private Boolean platformYn;

    /**
    * delete_yn - deleteYn
    */
    @TableLogic
    private Boolean deleteYn;

    public SysTenantDTO createDto() {
        SysTenantDTO dto = new SysTenantDTO();
        BeanUtils.copyProperties(this, dto);
        return dto;
    }

}