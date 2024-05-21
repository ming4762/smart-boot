package com.smart.sms.extensions.aliyun;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 阿里云通道参数
 * @author zhongming4762
 * 2023/5/25
 */
@Getter
@Setter
public class SmartSmsAliyunChannelProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 5132159022743250552L;

    private String accessKey;

    private String accessSecret;

    private String endpoint;
}
