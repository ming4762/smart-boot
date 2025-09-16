package com.smart.smc.inter.sdport.pay.pojo.parameter;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/10/30 15:41
 * @since 1.0.0
 */
@Getter
@Setter
public class PayCustomerRegisterInterfaceParameter extends PayCustomerRegisterParameter {
    @Serial
    private static final long serialVersionUID = -7910747182417167999L;

    /**
     * 系统来源，1：云港通2：舟道网;3：陆海通；4：渤海通；5：烟台港；6：海道网；7：山港一
     * 山东港口山港云付平台对接方案54
     * 卡通；8:山海超市；最大长度: 1
     * 最小长度: 1
     */
    private String fromSystem;
}
