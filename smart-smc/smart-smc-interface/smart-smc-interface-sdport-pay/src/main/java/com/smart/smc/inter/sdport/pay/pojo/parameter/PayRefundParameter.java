package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * 退款请求参数
 * @author shizhongming
 * 2025/9/15 14:37
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayRefundParameter implements Serializable {

    /**
     * 原支付订单号， 业务系统生成并
     * 请求上送的支付订单号
     */
    @NotNull(message = "原支付订单号不能为空")
    private String oldPayOutOrderNo;

    /**
     * 原支付发起时间，
     * yyyyMMddHHmmss
     */
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
    private ZonedDateTime oldTrxSendTime;

    /**
     * 退款发起时间， yyyyMMddHHmmss
     */
    @NotNull(message = "退款发起时间不能为空")
    @JsonFormat(pattern = "yyyyMMddHHmmss", timezone = "Asia/Shanghai")
    private ZonedDateTime trxSendTime;

    /**
     * 业务商户退款请求订单号
     */
    @NotNull(message = "业务商户退款请求订单号不能为空")
    private String refundOrderNo;

    /**
     * 平台商户号
     */
    @NotNull(message = "平台商户号不能为空")
    private String platMerCstNo;

    /**
     * 退款金额，最小值: 0.01
     */
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundOrdTransAmt;
}
