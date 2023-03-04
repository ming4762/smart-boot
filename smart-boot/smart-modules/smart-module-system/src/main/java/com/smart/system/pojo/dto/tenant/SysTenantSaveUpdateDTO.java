package com.smart.system.pojo.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* sys_tenant - 租户表
* @author SmartCodeGenerator
* 2023-2-26 12:18:21
*/
@Getter
@Setter
@ToString
@Schema(description = "租户添加修改")
public class SysTenantSaveUpdateDTO implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 租户编号
    */
    @Schema(description = "租户编号")
    @NotNull(message = "租户编号不能为空")
    private String tenantCode;
    /**
    * 租户名字
    */
    @Schema(description = "租户名字")
    @NotNull(message = "租户名称不能为空")
    private String tenantName;
    /**
    * 联系人
    */
    @Schema(description = "联系人")
    private String contacts;
    /**
    * 联系人电话
    */
    @Schema(description = "联系人电话")
    private String contactPhone;
    /**
    * 域名
    */
    @Schema(description = "域名")
    private String domain;
    /**
    * 可用人数，-1不限制
    */
    @Schema(description = "可用人数，-1不限制")
    private Long availableUserNum;
    /**
    * 地址
    */
    @Schema(description = "地址")
    private String address;
    /**
    * LOGO
    */
    @Schema(description = "LOGO")
    private Long logoId;
    /**
    * 开始时间
    */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    /**
    * 过期时间
    */
    @Schema(description = "过期时间")
    private LocalDateTime endTime;
    /**
    * 备注
    */
    @Schema(description = "备注")
    private String remark;
    /**
    * 序号
    */
    @Schema(description = "序号")
    @NotNull(message = "序号不能为空")
    private Integer seq;
    /**
    * 
    */
    @Schema(description = "启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean useYn;

}