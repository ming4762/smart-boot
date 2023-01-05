package com.smart.file.core.constants;

import lombok.Getter;

/**
 * @author ShiZhongMing
 * 2020/12/7 15:01
 * @since 1.0
 */
@Getter
public enum ActualFileServiceEnum {

    /**
     * 磁盘文件
     */
    DISK("ActualFileDiskService"),
    MONGO("ActualFileMongoService"),
    SFTP("ActualFileSftpService"),
    MINIO("ActualFileMinioService");


    private final String serviceName;

    ActualFileServiceEnum(String serviceName) {
        this.serviceName = serviceName;
    }
}
