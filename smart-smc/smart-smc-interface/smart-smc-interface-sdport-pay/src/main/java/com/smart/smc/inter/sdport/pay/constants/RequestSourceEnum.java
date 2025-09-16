package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

/**
 * 请求来源，0-web，1-安卓，2-IOS,6-商户
 * @author shizhongming
 * 2024/10/29 11:09
 * @since 1.0.0
 */
@Getter
public enum RequestSourceEnum implements ConstantCode {

    WEB("0", "WEB"),
    ANDROID("1", "android"),
    IOS("2", "ios"),
    MERCHANT("6", "商户"),

    ;
    private final String code;

    private final String remark;

    RequestSourceEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
