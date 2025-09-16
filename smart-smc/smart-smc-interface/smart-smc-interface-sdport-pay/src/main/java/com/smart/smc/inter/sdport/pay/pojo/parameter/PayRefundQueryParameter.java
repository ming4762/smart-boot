package com.smart.smc.inter.sdport.pay.pojo.parameter;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * 退款查询参数
 * @author shizhongming
 * 2025/9/15 16:58
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayRefundQueryParameter implements Serializable {

    /**
     * 业务商户退款订单号
     */
    @NotNull(message = "业务商户退款订单号不能为空")
    private String refundOrderNo;

    /**
     * 平台商户号
     */
    @NotNull(message = "平台商户号不能为空")
    private String platMerCstNo;

    /**
     * 退款申请时间
     * TODO:格式化
     */
    @NotNull(message = "退款申请时间不能为空")
    private String trxSendTime;
}
