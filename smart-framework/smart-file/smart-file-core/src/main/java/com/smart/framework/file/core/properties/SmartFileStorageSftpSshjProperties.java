package com.smart.framework.file.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * SFTP SSHJ 存储器属性
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-21 18:35
 * @since 5.0.0
 */
@Getter
@Setter
public class SmartFileStorageSftpSshjProperties implements Serializable {

    private String basePath;

    private String host;
    private Integer port;
    private String username;
    private String password;
    /**
     * 密钥认证时使用
     */
    private String privateKeyPath;

    private Integer connectTimeout;
}
