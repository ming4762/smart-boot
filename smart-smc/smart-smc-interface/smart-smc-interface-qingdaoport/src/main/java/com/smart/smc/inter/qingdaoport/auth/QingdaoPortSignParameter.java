package com.smart.smc.inter.qingdaoport.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 云港通签名参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-05 16:03
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class QingdaoPortSignParameter {

    private String customerCode;

    private String type;

    private String data;
}
