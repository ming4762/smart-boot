package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 山港云付接口地址
 * @author shizhongming
 * 2024/10/29 13:46
 * @since 1.0.0
 */
@Getter
public enum SdportPayUrlEnum {

    /**
     * 收银台下单
     */
    CREATE_ORDER("/api/wc/orderPay/createOrder", "收银台下单"),

    CUSTOMER_REGISTER("/api/wc/cstManage/register", "客户开通支付"),

    WAKEUP_CASHIER("/api/wc/cashier/wakeupCashier", "唤起收银台"),

    CANCEL_ORDER("/api/wc/orderPay/cancelOrder", "订单取消"),

    QUERY_ORDER("/api/wc/orderPay/queryOrder", "查询订单"),

    /**
     * 退款
     */
    REFUND("/api/wc/refundOrderInf/refund", "退款"),
    REFUND_QUERY("/api/wc/refundOrderInf/query", "退款查询"),
    ;

    private final String url;

    private final String remark;


    SdportPayUrlEnum(String url, String remark) {
        this.url = url;
        this.remark = remark;
    }
}
