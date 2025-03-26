package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 租户信息
 * @author shizhongming
 * 2025/3/26 17:53
 * @since 5.0.0
 */
@Getter
@Setter
public class SysTenantDTO implements Serializable {

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

}
