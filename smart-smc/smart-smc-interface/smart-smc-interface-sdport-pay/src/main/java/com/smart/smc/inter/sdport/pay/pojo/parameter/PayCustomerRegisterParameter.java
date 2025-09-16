package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.smc.inter.sdport.pay.constants.BusinessCstIdTypeEnum;
import com.smart.smc.inter.sdport.pay.constants.CustomerTypeEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/30 15:28
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayCustomerRegisterParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = 2700959947839231634L;

    /**
     * 业务系统客户号(<=32位)不允许重复支持数字、字母
     */
    @NotNull
    private String businessCstNo;

    /**
     * 客户证件类型个人客户："1" 身份证（非必传）企业客户："11"统一社会信用代码（必传）
     */
    @JsonSerialize(using = JacksonConverter.ConstantCodeSerializer.class)
    private BusinessCstIdTypeEnum businessCstIdType;

    /**
     * 客户证件号（身份证/社会信用代码<=18 位）同一系统来源下不允许重复个人客户：非必传企业客户：必传
     */
    private String businessCstIdNum;

    /**
     * 客户名称(<=50 位)个人客户：姓名（非必传）企业客户：企业名称（必传）
     */
    private String businessCstName;

    /**
     * 初始管理员手机号
     */
    @NotNull
    private String businessCstMobileNo;

    /**
     * 客户类型1-企业2-个人
     */
    @NotNull
    @JsonSerialize(using = JacksonConverter.ConstantCodeSerializer.class)
    private CustomerTypeEnum cstType;
}
