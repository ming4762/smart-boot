package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 客户类型
 * @author shizhongming
 * 2024/11/9 16:50
 * @since 1.0.0
 */
@Getter
public enum CustomerTypeEnum implements ConstantCode {

    /**
     * 客户类型1-企业2-个人
     */
    ENTERPRISE("1", "企业"),
    PERSONAL("2", "个人")
    ;

    private final String code;

    private final String description;

    CustomerTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
