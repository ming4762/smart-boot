package com.smart.auth.extensions.sms.provider;

import org.springframework.lang.NonNull;

/**
 * @author ShiZhongMing
 * 2021/6/4 9:08
 * @since 1.0
 */
public interface SmsCreateValidateProvider {

    /**
     * 创建验证码
     * @param phone 手机号
     * @return 验证码
     */
    String create(@NonNull String phone);


    /**
     * 验证验证码是否有效
     * @param phone 手机号
     * @param code 验证码
     * @return 验证结果
     */
    boolean validate(@NonNull String phone, String code);
}
