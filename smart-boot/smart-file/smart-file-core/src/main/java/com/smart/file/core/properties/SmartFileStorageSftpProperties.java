package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * NFS存储器配置
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
public class SmartFileStorageSftpProperties {

    private String host;

    private Integer port = 22;

    private String basePath;

    private String username;

    private String password;

    private String privateKey;
}
