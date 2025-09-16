package com.smart.smc.inter.sdport.pay.pojo.parameter;

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
import java.util.List;

/**
 * 支付结果通知接口
 * @author shizhongming
 * 2024/10/29 17:34
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayResultNotifyParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -310160262851341315L;

    private String sign;

    /**
     * 业务平台批次号
     */
    @NotNull
    private String batchNo;


    /**
     * 支付方式
     */
    @JsonDeserialize(using = JacksonConverter.PayTypeEnumDeserializer.class)
    private PayTypeEnum payType;

    /**
     * 订单状态10-初始状态60-交易中70-交易失败80-交易关闭90-支付成功
     */
    @NotNull
    @JsonDeserialize(using = JacksonConverter.OrderStatusEnumDeserializer.class)
    private OrderStatusEnum status;

    /**
     * 渠道编号
     */
    @JsonDeserialize(using = JacksonConverter.PayChannelEnumDeserializer.class)
    private PayChannelEnum chlNo;

    private List<OrderListParameter> orderList;


    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderListParameter implements Serializable {

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

        /**
         * 付款账号（一般为银行卡号）
         */
        private String payBankAccNo;

        /**
         * 付款账户名
         */
        private String payBankAccName;
    }
}
