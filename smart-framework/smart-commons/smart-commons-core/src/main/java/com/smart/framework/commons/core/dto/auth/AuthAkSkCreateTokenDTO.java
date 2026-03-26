package com.smart.framework.commons.core.dto.auth;

import lombok.*;
import org.springframework.http.HttpMethod;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

/**
 * 认证-创建签名
 * @author shizhongming
 * 2025/9/10 14:20
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AuthAkSkCreateTokenDTO implements Serializable {

    @NonNull
    private HttpMethod httpMethod;

    private String contentType;

    @NonNull
    private String nonce;

    private String parameterStr;

    @NonNull
    private String prefix;

    @NonNull
    private String accessKey;

    @NonNull
    private String secretKey;
}
