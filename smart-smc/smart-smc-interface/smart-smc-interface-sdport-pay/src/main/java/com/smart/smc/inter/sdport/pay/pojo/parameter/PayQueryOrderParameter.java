package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
 * 订单结果查询 参数
 * @author shizhongming
 * 2024/10/30 17:25
 * @since 1.0.0
 */
@Getter
@Setter
public class PayQueryOrderParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = 7750357302112694621L;

    /**
     * 支付平台商户号
     */
    @NotNull
    private String platMerCstNo;

    /**
     * 业务平台批次号
     */
    @NotNull
    private String batchNo;

    private List<OrderDetailParameter> orderDetailList;

    @Getter
    @Setter
    public static class OrderDetailParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = -1006289513786731376L;

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
        @NotNull
        @JsonSerialize(using = JacksonConverter.LocalDateTimeTimestampSerializer.class)
        private BigDecimal actualyAmt;

        /**
         * 成功金额
         */
        @NotNull
        @JsonSerialize(using = JacksonConverter.LocalDateTimeTimestampSerializer.class)
        private BigDecimal succTrxAmt;

        /**
         * 商户手续费
         */
        @NotNull
        @JsonSerialize(using = JacksonConverter.LocalDateTimeTimestampSerializer.class)
        private BigDecimal merFeeAmt;

        /**
         * 成功时间
         */
        @NotNull
        private LocalDateTime succTime;
    }

}
