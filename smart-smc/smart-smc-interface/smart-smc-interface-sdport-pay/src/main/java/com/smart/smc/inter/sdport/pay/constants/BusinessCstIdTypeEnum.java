package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 客户证件类型个人客户："1" 身份证（非必传）企业客户："11"统一社会信用代码（必传）
 * @author shizhongming
 * 2024/10/30 15:35
 * @since 1.0.0
 */
@Getter
public enum BusinessCstIdTypeEnum implements ConstantCode {

    /**
     * 客户证件类型个人客户："1" 身份证（非必传）企业客户："11"统一社会信用代码（必传）
     */
    IDENTITY_CARD("1", "身份证（非必传）"),
    UNIFIED_SOCIAL_CREDIT_CODE("11", "统一社会信用代码（必传）")

    ;

    private final String code;

    private final String description;

    BusinessCstIdTypeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }
}
