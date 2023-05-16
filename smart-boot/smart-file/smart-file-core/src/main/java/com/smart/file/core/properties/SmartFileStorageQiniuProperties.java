package com.smart.file.core.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 七牛云配置参数
 * @author zhongming4762
 * 2023/2/16
 */
@Getter
@Setter
public class SmartFileStorageQiniuProperties {

    private String accessKey;

    private String secretKey;

    private String bucketName;

    /**
     * 区域
     */
    private String region;

    private String url;

    /**
     * 是否使用https
     */
    private Boolean useHttps;
}
