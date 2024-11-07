package com.smart.framework.auth.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * SAML2认证配置
 * @author shizhongming
 * 2024/11/6 17:02
 * @since 5.0.0
 */
@Getter
@Setter
public class AuthSaml2Properties implements Serializable {

    @Serial
    private static final long serialVersionUID = 3228416613595572872L;

    private String entityId;

    private KeyStore key = new KeyStore();

    private Identity identity = new Identity();

    /**
     * entityBaseURL
     */
    private String entityBaseUrl;

    /**
     * 重试次数
     */
    private Integer retry = 5;

    /**
     * key 配置
     */
    @Getter
    @Setter
    public static class KeyStore {
        private String name;

        private String password;

        private String filePath;
    }

    @Getter
    @Setter
    public static class Identity {
        private Boolean discoveryEnabled = Boolean.TRUE;

        private String metadataFilePath;
    }
}
