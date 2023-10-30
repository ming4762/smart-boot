package com.smart.auth.core.secret.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shizhongming
 * 2023/10/26 19:31
 * @since 3.0.0
 */
@Getter
@AllArgsConstructor
public class AccessSecretData implements Serializable {

    private final String accessKey;

    private final String secretKey;

    private final LocalDateTime expireDate;

    private  final String accessIp;
}
