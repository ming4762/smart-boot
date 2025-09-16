package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 支付订单状态
 * @author shizhongming
 * 2024/10/29 17:36
 * @since 1.0.0
 */
@Getter
public enum OrderStatusEnum implements ConstantCode {

    /**
     * 订单状态10-初始状态60-交易中70-交易失败80-交易关闭90-支付成功
     */
    INIT("10", ""),

    PAYING("60", ""),

    FAIL("70", ""),

    CLOSED("80", ""),

    SUCCESS("90", ""),
    ;

    private final String code;

    private final String remark;

    OrderStatusEnum(final String code, final String remark) {
        this.code = code;
        this.remark = remark;
    }

    public static OrderStatusEnum getByCode(String code) {
        return Stream.of(OrderStatusEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
