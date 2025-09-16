package com.smart.smc.inter.sdport.pay.pojo.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 收银台下单返回结果
 * @author shizhongming
 * 2024/10/29 13:39
 * @since 1.0.0
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPayCreateOrderResult implements Serializable {

    @Serial
    private static final long serialVersionUID = -1726475341459371598L;
    /**
     * 平台商户号
     */
    @NotNull
    private String platMerCstNo;

    /**
     * 交易金额
     */
    @NotNull
    private BigDecimal trxAmt;

    /**
     * 业务平台批次号，原样输出
     */
    @NotNull
    private String batchNo;

    private List<OrderListResult> orderList;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderListResult implements Serializable {

        @Serial
        private static final long serialVersionUID = -5339371948849350719L;
        @NotNull
        private String orderNo;

        @NotNull
        private String currency;

        @NotNull
        private BigDecimal trxAmt;
    }
}
