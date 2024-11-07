package com.smart.module.system.pojo.dto.tenant;

import com.smart.module.system.constants.SysTenantIsolationStrategyEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * sys_tenant - 租户表
 * @author SmartCodeGenerator
 * 2024年3月29日 下午1:40:04
 */
@Getter
@Setter
@ToString
public class SysTenantSaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -8257924948155931757L;
    /**
     * id
     */
    private Long id;
    /**
     * 租户编号
     * todo：国际化
     */
    @NotNull(message = "租户编码不能为空")
    private String tenantCode;
    /**
     * 租户名字
     */
    @NotNull(message = "租户名称不能为空")
    private String tenantName;
    /**
     * 简称
     */
    private String tenantShortName;
    /**
     * 类型
     */
    @NotNull(message = "租户类型不能为空")
    private String type;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 隔离策略：FIELD、TABLE、DATABASE
     */
    private SysTenantIsolationStrategyEnum isolationStrategy;
    /**
     * 行业
     */
    private String industry;
    /**
     * 域名
     */
    private String domain;
    /**
     * 可用人数，-1不限制
     */
    private Long availableUserNum;
    /**
     * 地区
     */
    private String region;
    /**
     * 地址
     */
    private String address;
    /**
     * LOGO
     */
    private Long logoId;
    /**
     * 生效时间
     */
    private LocalDateTime effectTime;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 序号
     */
    private Integer seq;

}