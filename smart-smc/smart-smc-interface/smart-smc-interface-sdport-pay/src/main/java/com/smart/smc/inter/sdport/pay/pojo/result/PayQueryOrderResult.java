package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smart.smc.inter.sdport.pay.constants.OrderStatusEnum;
import com.smart.smc.inter.sdport.pay.constants.PayChannelEnum;
import com.smart.smc.inter.sdport.pay.constants.PayTypeEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单结果查询 结果
 * @author shizhongming
 * 2024/10/30 17:26
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayQueryOrderResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 3703533843295727879L;

    /**
     * 支付订单状态
     */
    @NotNull
    @JsonDeserialize(using = JacksonConverter.OrderStatusEnumDeserializer.class)
    private OrderStatusEnum status;

    /**
     * 成功时间，交易成功时返回，格式：yyyyMMddHHmmss
     */
    private LocalDateTime succTime;

    /**
     * 成功金额，交易成功时返回
     */
    private BigDecimal succTrxAmt;

    /**
     * 前台通知地址
     */
    @NotNull
    private String payReturnUrl;

    /**
     * 支付方式,
     */
    private PayTypeEnum payType;

    /**
     * 渠道编号
     */
    @NotNull
    private PayChannelEnum chlNo;

    /**
     * 付款账号（一般为银行卡号）
     */
    private String payBankAccNo;

    /**
     * 付款账户名
     */
    @NotNull
    private String payBankAccName;

    private List<OrderListResult> orderDetailList;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderListResult implements Serializable {

        @Serial
        private static final long serialVersionUID = 5708204127742775538L;

        /**
         * 商户订单号
         */
        @NotNull
        private String orderNo;

        /**
         * 支付中心订单号
         */
        @NotNull
        private String orderFlowNo;

        /**
         * 实付金额
         */
        private BigDecimal actualyAmt;

        /**
         * 成功金额
         */
        private BigDecimal succTrxAmt;

        /**
         * 商户手续费
         */
        private BigDecimal merFeeAmt;

        /**
         * TODO：格式需要调整
         * 成功时间
         */
        private String succTime;
    }
}
