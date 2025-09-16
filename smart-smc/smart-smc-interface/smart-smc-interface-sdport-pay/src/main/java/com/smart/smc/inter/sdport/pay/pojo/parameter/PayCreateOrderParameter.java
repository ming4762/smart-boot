package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.smc.inter.sdport.pay.constants.OrderSourceEnum;
import com.smart.smc.inter.sdport.pay.jackson.JacksonConverter;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shizhongming
 * 2024/10/29 10:54
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayCreateOrderParameter implements Serializable {


    @Serial
    private static final long serialVersionUID = -9152747556340108333L;
    /**
     * 业务平台付款客户号，最长 32 位，支持数字、字母组成
     */
    @NotNull
    private String businessCstNo;

    /**
     * 收款商户号
     * // TODO:需要考虑多个收款账号
     * 调用商户入网接口时，山港云付平台生成的商户ID
     */
    @NotNull
    private String platMerCstNo;

    /**
     * 批次号
     */
    @NotNull
    private String batchNo;


    /**
     * 场景：00-PC，01-C 端APP，02-H5，07-B 端APP，08-B 端POS
     */
    @NotNull
    @JsonSerialize(using = JacksonConverter.ConstantCodeSerializer.class)
    private OrderSourceEnum orderSource;

    /**
     * 付款用户姓名
     * 最大长度:50
     */
    private String payerName;

    /**
     * 付款用户手机号
     * 最大长度:11
     */
    private String payerMobileNo;


    /**
     * 订单过期时间（格式：时间戳）
     */
    @JsonSerialize(using = JacksonConverter.LocalDateTimeTimestampSerializer.class)
    private LocalDateTime expiredTime;


    private List<OrderListParameter> orderList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderListParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = 2452630404764016934L;

        /**
         * 业务系统订单号，需要业务系统下唯一，支持数字、字母组成
         */
        @NotNull
        private String orderNo;

        /**
         * 币种，固定-CNY
         */
        @NotNull
        private String currency;


        /**
         * 备注，支持数字、字母、文字、符号（仅支持-和英文,）此字段会上送银行渠道展示在电子回单内，如需要按照业务板块进行资金分笔结算的商户订单必须上传业务板块名称，同一业务板块名称必须一致，最大长度:120字符
         */
        @NotNull
        private String remark;

        /**
         * 商品名称
         */
        @NotNull
        private String goodsInfo;

        /**
         * 交易金额（元）
         */
        @NotNull
        @JsonSerialize(using = JacksonConverter.BigDecimalStringSerializer.class)
        private BigDecimal trxAmt;

        /**
         * 交易商户号，自营时同平台商户号
         */
        @NotNull
        private String tradeMerCstNo;
    }

}
