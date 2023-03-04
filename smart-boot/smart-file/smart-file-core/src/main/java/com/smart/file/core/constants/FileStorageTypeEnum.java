package com.smart.file.core.constants;

import com.smart.file.core.properties.SmartFileStorageAliyunOssProperties;
import com.smart.file.core.properties.SmartFileStorageDiskProperties;
import com.smart.file.core.properties.SmartFileStorageMinioProperties;
import com.smart.file.core.properties.SmartFileStorageSftpProperties;
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
    DISK("ActualFileDiskService", SmartFileStorageDiskProperties.class),
    SFTP("ActualFileSftpService", SmartFileStorageSftpProperties.class),
    MINIO("ActualFileMinioService", SmartFileStorageMinioProperties.class),
    ALIYUN_OSS("ActualFileAliyunOssService", SmartFileStorageAliyunOssProperties.class)
    ;

    @Getter
    private final String serviceName;

    @Getter
    private final Class<?> propertiesClass;

    FileStorageTypeEnum(String serviceName, Class<?> propertiesClass) {
        this.serviceName = serviceName;
        this.propertiesClass = propertiesClass;
    }
}
