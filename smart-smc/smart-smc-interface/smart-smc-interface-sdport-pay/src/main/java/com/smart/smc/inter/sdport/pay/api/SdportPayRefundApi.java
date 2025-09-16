package com.smart.smc.inter.sdport.pay.api;

import com.smart.smc.inter.sdport.pay.pojo.parameter.PayRefundParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayRefundQueryParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.PayRefundResult;

/**
 * 支付退款接口
 * @author shizhongming
 * 2025/9/15 14:34
 * @since 1.0.0
 */
public interface SdportPayRefundApi {

    /**
     * 退款
     * @param parameter 退款参数
     * @return 退款结果
     */
    PayRefundResult refund(PayRefundParameter parameter);

    /**
     * 退款查询
     * @param parameter 退款查询参数
     * @return 退款查询结果
     */
    PayRefundResult query(PayRefundQueryParameter parameter);
}
