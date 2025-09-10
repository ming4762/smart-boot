package com.smart.auth.extensions.access.secret.constants;

import lombok.Getter;

/**
 * 签名字段枚举
 * @author shizhongming
 * 2025/9/9 16:28
 * @since 5.0.0
 */
@Getter
public enum AccessSignatureEnum {

    X_SIGNATURE_BODY_HASH("X-Signature-Body-Hash", "请求体hash值"),
    ;

    private final String key;

    private final String remark;

    AccessSignatureEnum(String key, String remark) {
        this.key = key;
        this.remark = remark;
    }
}
