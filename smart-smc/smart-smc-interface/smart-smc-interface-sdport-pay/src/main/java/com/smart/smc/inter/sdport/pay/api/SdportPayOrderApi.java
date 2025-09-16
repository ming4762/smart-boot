package com.smart.smc.inter.sdport.pay.api;

import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCreateOrderParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayQueryOrderParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayWakeupCashierParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.OrderPayCreateOrderResult;
import com.smart.smc.inter.sdport.pay.pojo.result.PayQueryOrderResult;

/**
 * 支付相关接口
 * @author shizhongming
 * 2024/10/29 10:52
 * @since 1.0.0
 */
public interface SdportPayOrderApi {

    /**
     * 收银台下单
     * 此接口用于客户在业务平台发起付款时业务平台在山港云付平台预下单使用，预下单完成后调用唤起收银台接口让用户跳转至收银台。
     * @param parameter 参数
     * @return 结果
     */
    OrderPayCreateOrderResult createOrder(PayCreateOrderParameter parameter);

    /**
     * 唤起收银台
     * 本接口用于业务平台在调用收银台下单接口成功后唤起统一收银台使用，收银台支持 PC、H5、APP，用户跳转收银台选择支付方式进行付款，本接口为 form 表单提交
     * @param parameter 参数
     * @return 结果
     */
    String wakeupCashier(PayWakeupCashierParameter parameter);

    /**
     * 订单取消
     * 本接口用于将未支付订单置成取消状态
     * @param batchNo 批次号
     * @return 是否取消成功
     */
    boolean cancelOrder(String batchNo);

    /**
     * 订单结果查询
     * 本接口用于业务平台查询订单信息使用，可用于查询支付方式和支付状态等数据，可在系统掉单时主动补单时使用。
     * @param parameter 参数
     * @return 结果
     */
    PayQueryOrderResult queryOrder(PayQueryOrderParameter parameter);
}
