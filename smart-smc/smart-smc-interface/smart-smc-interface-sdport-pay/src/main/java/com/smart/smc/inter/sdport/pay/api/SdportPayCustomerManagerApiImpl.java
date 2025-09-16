package com.smart.smc.inter.sdport.pay.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smart.module.api.system.SysLogApi;
import com.smart.module.api.system.SysParameterApi;
import com.smart.smc.inter.sdport.pay.SmartSmcSdportPayProperties;
import com.smart.smc.inter.sdport.pay.constants.SdportPayUrlEnum;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCustomerRegisterInterfaceParameter;
import com.smart.smc.inter.sdport.pay.pojo.parameter.PayCustomerRegisterParameter;
import com.smart.smc.inter.sdport.pay.pojo.result.PayCustomerRegisterResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author shizhongming
 * 2024/10/30 15:40
 * @since 1.0.0
 */
@Component
public class SdportPayCustomerManagerApiImpl extends CommonApi implements SdportPayCustomerManagerApi {

    private final SmartSmcSdportPayProperties properties;

    public SdportPayCustomerManagerApiImpl(SmartSmcSdportPayProperties properties, SysLogApi sysLogApi, SysParameterApi sysParameterApi) {
        super(sysLogApi, sysParameterApi);
        this.properties = properties;
    }

    /**
     * 本接口用于业务平台给客户在山港云付平台开通支付权限
     *
     * @param parameter 参数
     * @return 结果
     */
    @Override
    public PayCustomerRegisterResult register(PayCustomerRegisterParameter parameter) {
        PayCustomerRegisterInterfaceParameter interfaceParameter = new PayCustomerRegisterInterfaceParameter();

        BeanUtils.copyProperties(parameter, interfaceParameter);

        interfaceParameter.setFromSystem(this.properties.getFromSystem());
        return this.doRequest(SdportPayUrlEnum.CUSTOMER_REGISTER, interfaceParameter, new TypeReference<>() {
        });
    }
}
