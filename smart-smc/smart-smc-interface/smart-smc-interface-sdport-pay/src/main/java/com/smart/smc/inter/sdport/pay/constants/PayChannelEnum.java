package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 渠道编号1005-支付宝1006-微信1007-平安银行云收款1010-银联商务1011-青岛银行
 * 山东港口山港云付平台对接方案45
 * 1014-青岛银行企业网关支付1012-工商银行2001-平安银行B2B结算通2002-工行安心账户
 * @author shizhongming
 * 2024/10/30 8:54
 * @since 1.0.0
 */
@Getter
public enum PayChannelEnum implements ConstantCode {

    /**
     * 渠道编号1005-支付宝1006-微信1007-平安银行云收款1010-银联商务1011-青岛银行
     * 山东港口山港云付平台对接方案45
     * 1014-青岛银行企业网关支付1012-工商银行2001-平安银行B2B结算通2002-工行安心账户
     */
    ALIPAY("1005", "支付宝"),

    WECHAT("1006", "微信"),

    PING_AN("1007", "平安银行云收款"),

    YIN_LIAN("1010", "银联商务"),

    QING_DAO("1011", "青岛银行"),

    QING_DAO_GATEWAY("1014", "青岛银行企业网关支付"),

    ICBC("1012", "工商银行"),

    PING_AN_B2B("2001", "平安银行B2B结算通"),

    ICBC_ANXIN("2002", "工行安心账户"),
    ;

    private final String code;

    private final String description;

    private PayChannelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PayChannelEnum getByCode(String code) {
        return Stream.of(PayChannelEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
