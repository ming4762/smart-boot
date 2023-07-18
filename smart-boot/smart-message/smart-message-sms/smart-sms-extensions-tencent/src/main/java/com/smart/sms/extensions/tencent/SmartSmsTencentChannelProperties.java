package com.smart.sms.extensions.tencent;

import com.smart.sms.extensions.tencent.constants.TencentRegionEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 阿里云通道参数
 * @author zhongming4762
 * 2023/5/25
 */
@Getter
@Setter
public class SmartSmsTencentChannelProperties implements Serializable {

    private String accessKey;

    private String accessSecret;

    /**
     * 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId，示例如1400006666
     * 应用 ID 可前往(<a href="https://console.cloud.tencent.com/smsv2/app-manage">短信控制台</a>) 查看
     */
    private String appid;

    /**
     * 地域信息
     */
    private TencentRegionEnum region;
}
