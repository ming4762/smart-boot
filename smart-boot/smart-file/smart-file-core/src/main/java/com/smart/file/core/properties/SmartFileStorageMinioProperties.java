package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhongming4762
 * 2023/2/16 22:09
 */
@Getter
@Setter
public class SmartFileStorageMinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
