package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.smart.smc.inter.sdport.pay.constants.PayRefundStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款结果通知参数
 * @author shizhongming
 * 2025/9/15 14:59
 * @since 1.0.0
 */
@Getter
@Setter
public class PayRefundNotifyParameter implements Serializable {

    /**
     * 退款订单状态
     */
    private PayRefundStatusEnum refundOrdStatus;

    /**
     * 业务商户申请退款单号
     */
    private String refundOrderNo;

    /**
     * 退款金额
     */
    private BigDecimal refundOrdTransAmt;

    /**
     * 退款成功时间
     */
    private Long refundSuccTime;

    /**
     * 退款商户名称
     */
    private String refundOrdMerName;

    /**
     * 退款手续费金额
     */
    private BigDecimal refundOrdFeeAmt;

    /**
     * 商品备注
     */
    private String refundOrdRemark;

    /**
     * 交易类型 2001
     */
    private String refundOrdTransType;

    /**
     * 通知地址
     */
    private String notifyURL;

    /**
     * 退款币种
     */
    private String refundOrdCurrencyNo;

    /**
     * 失败原因
     */
    private String errMsg;
}
