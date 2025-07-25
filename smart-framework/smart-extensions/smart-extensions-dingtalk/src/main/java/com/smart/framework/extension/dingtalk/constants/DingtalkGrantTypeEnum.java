package com.smart.framework.extension.dingtalk.constants;

import lombok.Getter;

/**
 * 钉钉授权类型
 * @author shizhongming
 * 2025/7/17 15:08
 * @since 5.0.0
 */
@Getter
public enum DingtalkGrantTypeEnum {

    AUTHORIZATION_CODE("authorization_code", "使用授权码获取用户token"),
    REFRESH_TOKEN("refresh_token", "使用刷新token获取用户token"),
    ;

    private final String grantType;

    private final String description;

    DingtalkGrantTypeEnum(String grantType, String description) {
        this.grantType = grantType;
        this.description = description;
    }

}
