package com.smart.smc.inter.sdport.pay.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.framework.commons.validate.utils.ValidatorUtils;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysParameterApi;
import com.smart.smc.inter.sdport.pay.constants.NotifyUrlEnum;
import com.smart.smc.inter.sdport.pay.constants.SdportPayUrlEnum;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayRefundInterfaceParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayRefundParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayRefundQueryParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.PayRefundResult;
import org.springframework.stereotype.Component;

/**
 * 支付退款接口实现类
 * @author shizhongming
 * 2025/9/15 16:18
 * @since 1.0.0
 */
@Component
public class SdportPayRefundApiImpl extends CommonApi implements SdportPayRefundApi {

    public SdportPayRefundApiImpl(SysLogApi sysLogApi, SysParameterApi sysParameterApi) {
        super(sysLogApi, sysParameterApi);
    }

    /**
     * 退款
     *
     * @param parameter 退款参数
     * @return 退款结果
     */
    @Override
    public PayRefundResult refund(PayRefundParameter parameter) {
        // 校验参数
        ValidatorUtils.validate(parameter);

        PayRefundInterfaceParameter interfaceParameter = PayRefundInterfaceParameter.create(parameter);
        // 设置通知地址
        interfaceParameter.setNotifyUrl(this.getNotifyUrl(NotifyUrlEnum.REFUND_NOTIFY_URL));
        return this.doRequest(SdportPayUrlEnum.REFUND, interfaceParameter, new TypeReference<>() {
        });
    }

    /**
     * 退款查询
     *
     * @param parameter 退款查询参数
     * @return 退款查询结果
     */
    @Override
    public PayRefundResult query(PayRefundQueryParameter parameter) {
        // 校验参数
        ValidatorUtils.validate(parameter);
        return this.doRequest(SdportPayUrlEnum.REFUND_QUERY, parameter, new TypeReference<>() {
        });
    }
}
