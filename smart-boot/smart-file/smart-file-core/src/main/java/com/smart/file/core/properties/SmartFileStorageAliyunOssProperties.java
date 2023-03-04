package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 阿里云OSS参数类
 * @author zhongming4762
 * 2023/3/4
 */
@Getter
@Setter
public class SmartFileStorageAliyunOssProperties implements Serializable {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
