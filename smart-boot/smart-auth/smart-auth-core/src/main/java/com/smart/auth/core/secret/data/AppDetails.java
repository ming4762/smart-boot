package com.smart.auth.core.secret.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * app信息
 * @author ShiZhongMing
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class AppDetails {

    private String appId;

    private String appSecret;

    private List<String> whiteIpList;
}
