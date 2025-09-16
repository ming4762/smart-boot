package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 通知地址常量枚举
 * @author shizhongming
 * 2025/9/15 14:48
 * @since 1.0.0
 */
@Getter
public enum NotifyUrlEnum {

    PAY_NOTIFY_URL("/sd-port/notify/payNotify", "支付通知地址"),
    REFUND_NOTIFY_URL("/sd-port/notify/refundNotify", "退款通知地址"),
    ;

    private final String url;

    private final String remark;

    NotifyUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
