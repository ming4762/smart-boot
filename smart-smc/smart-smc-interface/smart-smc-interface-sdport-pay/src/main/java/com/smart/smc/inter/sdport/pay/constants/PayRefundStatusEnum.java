package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 退款状态枚举
 * @author shizhongming
 * 2025/9/15 14:53
 * @since 1.0.0
 */
@Getter
public enum PayRefundStatusEnum implements ConstantCode {

    /**
     * 退款状态，10-待退款，
     * 50-退款拒绝，60-退款
     * 中，70-退款失败，90-
     * 退款成功
     */
    WAIT_REFUND("10", "待退款"),
    REFUND_REJECT("50", "退款拒绝"),
    REFUNDING("60", "退款中"),
    REFUND_FAIL("70", "退款失败"),
    REFUND_SUCCESS("90", "退款成功")
    ;

    private final String code;

    private final String remark;

    PayRefundStatusEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public static PayRefundStatusEnum getByCode(String code) {
        return Stream.of(PayRefundStatusEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
