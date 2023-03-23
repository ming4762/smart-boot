package com.smart.module.api.file.constants;

import lombok.Getter;

/**
 * 文件存储器类型
 * @author zhongming4762
 * 2023/2/16
 */
public enum FileStorageTypeEnum {
    /**
     * 文件存储器类型
     */
    DISK("ActualFileDiskService", "com.smart.file.core.properties.SmartFileStorageDiskProperties"),
    SFTP("ActualFileSftpService", "com.smart.file.core.properties.SmartFileStorageSftpProperties"),
    MINIO("ActualFileMinioService", "com.smart.file.core.properties.SmartFileStorageMinioProperties"),
    ALIYUN_OSS("ActualFileAliyunOssService", "com.smart.file.core.properties.SmartFileStorageAliyunOssProperties")
    ;

    @Getter
    private final String serviceName;

    @Getter
    private final String propertiesClass;

    FileStorageTypeEnum(String serviceName, String propertiesClass) {
        this.serviceName = serviceName;
        this.propertiesClass = propertiesClass;
    }
}
