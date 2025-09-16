package com.smart.smc.inter.sdport.pay.pojo.parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 退款接口参数
 * @author shizhongming
 * 2025/9/15 16:21
 * @since 1.0.0
 */
@Getter
@Setter
public class PayRefundInterfaceParameter extends PayRefundParameter {

    /**
     * 退款结果通知地址
     */
    @JsonProperty("notifyURL")
    private String notifyUrl;

    public static PayRefundInterfaceParameter create(PayRefundParameter parameter) {
        PayRefundInterfaceParameter interfaceParameter = new PayRefundInterfaceParameter();
        BeanUtils.copyProperties(parameter, interfaceParameter);
        return interfaceParameter;
    }
}
