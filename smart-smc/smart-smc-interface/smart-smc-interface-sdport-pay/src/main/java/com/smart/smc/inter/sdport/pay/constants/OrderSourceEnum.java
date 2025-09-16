package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 上岗云付支付来源
 * @author shizhongming
 * 2024/10/29 10:59
 * @since 1.0.0
 */
@Getter
public enum OrderSourceEnum implements ConstantCode {

    PC("00", "PC端"),

    APP_C("01", "C端APP"),

    H5("02", "H5"),

    APP_B("07", "B 端APP"),

    POS_B("08", "-B 端POS"),
    ;

    private final String code;

    private final String remark;


    OrderSourceEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

}
