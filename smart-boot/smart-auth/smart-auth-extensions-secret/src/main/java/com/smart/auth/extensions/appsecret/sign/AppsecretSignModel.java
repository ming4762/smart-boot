package com.smart.auth.extensions.appsecret.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 签名实体
 * @author ShiZhongMing
 * @since 1.0
 */
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class AppsecretSignModel {


    private String timestamp;

    private String noncestr;

    private String accessToken;

    private String url;
}
