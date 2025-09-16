package com.smart.smc.inter.sdport.pay.api;

import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCustomerRegisterParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.PayCustomerRegisterResult;

/**
 * 山港云付 客户管理API
 * @author shizhongming
 * 2024/10/30 15:26
 * @since 1.0.0
 */
public interface SdportPayCustomerManagerApi {

    /**
     * 本接口用于业务平台给客户在山港云付平台开通支付权限
     * @param parameter 参数
     * @return 结果
     */
    PayCustomerRegisterResult register(PayCustomerRegisterParameter parameter);
}
