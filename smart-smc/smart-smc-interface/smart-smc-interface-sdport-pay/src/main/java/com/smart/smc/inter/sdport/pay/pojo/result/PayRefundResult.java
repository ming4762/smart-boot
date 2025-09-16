package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smart.smc.inter.sdport.pay.constants.PayRefundStatusEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款结果
 * @author shizhongming
 * 2025/9/15 14:52
 * @since 1.0.0
 */
@Getter
@Setter
public class PayRefundResult implements Serializable {

    /**
     * 业务商户请求退款订单号
     */
    private String refundOrderNo;

    /**
     * 支付中心退款订单号
     */
    private String refundOrderFlowNo;

    /**
     * 退款状态
     */
    @JsonDeserialize(using = JacksonConverter.PayRefundStatusDeserializer.class)
    private PayRefundStatusEnum refundOrdStatus;

    /**
     * 退款金额
     */
    private BigDecimal refundOrdTransAmt;

    /**
     * 退款成功时间
     * TODO：转为时间类型
     */
    private String refundSuccTime;

    /**
     * 退款商户名称
     */
    private String refundOrdMerName;

    /**
     * 退款手续费金额
     */
    private BigDecimal refundOrdFeeAmt;

    /**
     * 退款备注
     */
    private String refundOrdRemark;

    /**
     * 退款交易类型
     */
    private String refundOrdTransType;

    /**
     * 后台通知地址
     */
    @JsonProperty("notifyURL")
    private String notifyUrl;

    /**
     * 币种
     */
    private String refundOrdCurrencyNo;
}
