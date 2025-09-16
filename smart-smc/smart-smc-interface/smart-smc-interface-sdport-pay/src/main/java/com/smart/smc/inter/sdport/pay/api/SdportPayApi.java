package com.smart.smc.inter.sdport.pay.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 山港云付支付接口
 * @author shizhongming
 * 2024/10/29 10:48
 * @since 1.0.0
 */
@Component
public class SdportPayApi implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 订单API
     * @return 订单API
     */
    public SdportPayOrderApi sdportPayOrderApi() {
        return this.applicationContext.getBean(SdportPayOrderApi.class);
    }

    /**
     * 退款API
     * @return 退款API
     */
    public SdportPayRefundApi sdportPayRefundApi() {
        return this.applicationContext.getBean(SdportPayRefundApi.class);
    }


    /**
     * 用户管理API
     * @return 用户管理API
     */
    public SdportPayCustomerManagerApi sdportPayCustomerManagerApi() {
        return this.applicationContext.getBean(SdportPayCustomerManagerApi.class);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
