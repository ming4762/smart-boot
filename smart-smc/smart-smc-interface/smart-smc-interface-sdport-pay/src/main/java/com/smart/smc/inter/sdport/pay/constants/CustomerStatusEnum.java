package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 客户状态（10：待实名20：正常30：证件已过期40：冻结50：注销）
 * @author shizhongming
 * 2024/10/30 15:45
 * @since 1.0.0
 */
@Getter
public enum CustomerStatusEnum implements ConstantCode {

    /**
     * 客户状态（10：待实名20：正常30：证件已过期40：冻结50：注销）
     */
    REAL_NAME("10", "待实名"),

    NORMAL("20", "正常"),

    OVERDUE("30", "证件已过期"),

    FROZEN("40", "冻结"),

    CANCEL("50", "注销"),

    ;

    private final String code;

    private final String description;

    CustomerStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CustomerStatusEnum getByCode(String code) {
        return Stream.of(CustomerStatusEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
