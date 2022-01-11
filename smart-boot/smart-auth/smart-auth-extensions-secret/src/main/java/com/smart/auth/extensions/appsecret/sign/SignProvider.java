package com.smart.auth.extensions.appsecret.sign;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public interface SignProvider {

    /**
     * 对参数进行签名
     * @param parameter 签名参数
     * @return 签名
     */
    String sign(AppsecretSignModel parameter);
}
