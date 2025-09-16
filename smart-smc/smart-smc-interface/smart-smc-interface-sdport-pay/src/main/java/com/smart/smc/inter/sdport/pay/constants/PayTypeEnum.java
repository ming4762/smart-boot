package com.smart.smc.inter.sdport.pay.constants;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * 支付方式
 * @author shizhongming
 * 2024/10/29 17:40
 * @since 1.0.0
 */
@Getter
public enum PayTypeEnum implements ConstantCode {

    /**
     * 支付方式00-钱包支付02-个人网银03-B2B 网银11-微信APP 支付12-微信主扫支付14-微信小程序支付15-微信公众号21-支付宝APP支付22-支付宝主扫支付44-云闪付48-用户被扫50-签约支付52-数字人民币支付60-API 余额扣款
     */
    WALLET("00", "钱包支付02"),

    PERSONAL_BANKING("02", "个人网银03"),

    B2B("03", "B2B 网银"),

    WECHAT_APP("11", "微信APP"),

    WECHAT_SCAN("12", "微信主扫支付"),

    WECHAT_MIN_APP("14", "微信小程序支付"),

    WECHAT_OFFICIAL("15", "微信公众号"),

    ALIPAY_APP("21", "支付宝APP支付"),

    ALIPAY_SCAN("22", "支付宝主扫支付"),

    CLOUD_QUICK_PASS("44", "云闪付"),

    USER_SCANNED("48", "用户被扫"),

    CONTRACT_PAYMENT("50", "签约支付"),

    USER_SCAN("51", "用户主扫"),

    DIGITAL_RMB("52", "数字人民币支付"),

    PINGAN_BANK_B2B("53", "平安银行B2B 网银支付，平安银行"),

    PINGAN_BANK_CLOUD("58", "订单支付，平安银行云收款"),

    API_BALANCE("60", "-API 余额扣款"),
    ;

    private final String code;

    private final String description;

    PayTypeEnum(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static PayTypeEnum getByCode(String code) {
        return Stream.of(PayTypeEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
