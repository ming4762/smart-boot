package com.smart.smc.inter.sdport.pay.pojo.parameter;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/10/30 10:24
 * @since 1.0.0
 */
@Getter
@Setter
public class PayCreateOrderInterfaceParameter extends PayCreateOrderParameter {
    @Serial
    private static final long serialVersionUID = -7578305529398683107L;

    /**
     * 是否是担保支付0：普通支付 1：担保支付非必填，默认0：普通支付；注：若传担保支付则需支付成功后再次调用“担保支付订单确认收货”接口才可完成收款方入账
     * 应用于担保支付交易场景
     */
    private String guaranteedFlag;

    /**
     * 后台通知地址，用于业务方接收支付结果
     */
    private String payNotifyUrl;


    /**
     * 前台通知地址
     */
    private String payReturnUrl;

    /**
     * 传入 2001（支付）
     */
    @NotNull
    private String transType;
}
