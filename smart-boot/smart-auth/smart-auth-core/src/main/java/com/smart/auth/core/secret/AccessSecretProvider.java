package com.smart.auth.core.secret;

import com.smart.auth.core.secret.data.AccessSecretData;

/**
 * 获取认证信息
 * @author shizhongming
 * 2023/10/26 19:31
 * @since 3.0.0
 */
public interface AccessSecretProvider {

    /**
     * 通过 accessKey 获取认证信息
     * @param accessKey accessKey
     * @return 认证信息
     */
    AccessSecretData get(String accessKey);
}

