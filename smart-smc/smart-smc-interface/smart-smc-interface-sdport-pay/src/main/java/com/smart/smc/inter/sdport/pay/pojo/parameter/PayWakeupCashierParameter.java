package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.smc.inter.sdport.pay.constants.OrderSourceEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/10/30 15:53
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayWakeupCashierParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -6468959152402398726L;

    /**
     * 操作员手机号
     */
    @NotNull
    private String payPhone;

    /**
     * 业务平台客户号
     */
    @NotNull
    private String businessCstNo;

    /**
     * 平台商户号
     */
    @NotNull
    private String platMerCstNo;

    /**
     * 批次号
     */
    @NotNull
    private String batchNo;

    /**
     * 支付方式 ，可同时传多个，多个支付方式之间通过英文分号隔开，如不传此字段收银台则展示所有支付方式，以下每个支付方式“，”后面均为所使用的支付渠道
     * 枚举 PayTypeEnum
     */
//    @JsonSerialize(using = JacksonConverter.ConstantCodeSerializer.class)
    private String payType;

    /**
     *  接入场景 00:PC 01:C端APP 02:H507:B端 APP 08:B 端POS 09:码牌10:API小程序
     */
    @NotNull
    @JsonSerialize(using = JacksonConverter.ConstantCodeSerializer.class)
    private OrderSourceEnum orderSource;

    /**
     * 15-微信公众号，时必传
     */
    private String appid;

}
