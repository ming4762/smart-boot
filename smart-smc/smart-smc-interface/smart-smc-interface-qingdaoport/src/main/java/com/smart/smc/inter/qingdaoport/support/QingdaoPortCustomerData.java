package com.smart.smc.inter.qingdaoport.support;

/**
 * 云港通客户数据-编码-私钥-默认标志
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 15:57
 * @since 5.0.0
 */
public record QingdaoPortCustomerData(String customerCode, String privateKey, String sysCode, boolean defaultYn) {
}
