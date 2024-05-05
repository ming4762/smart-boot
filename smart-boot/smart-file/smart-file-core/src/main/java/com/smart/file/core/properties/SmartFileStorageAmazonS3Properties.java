package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Amazon S3 参数
 * @author shizhongming
 * 2024/4/24 20:40
 * @since 3.0.0
 */
@Getter
@Setter
public class SmartFileStorageAmazonS3Properties implements Serializable {

    @Serial
    private static final long serialVersionUID = 258110006129416892L;


    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
