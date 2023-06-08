package com.smart.module.api.message.parameter;

import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * remote短信发送参数
 * @author zhongming4762
 * 2023/6/6
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RemoteSmsSendParameter implements Serializable {

    /**
     * 手机号码列表
     */
    private List<String> phoneNumberList;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板
     */
    private String template;

    /**
     * 模板参数
     */
    private LinkedHashMap<String, String> templateParameter;

    /**
     * 通道ID，优先级比channel code高
     * 如果 channel id code 都为null，则使用默认的通道
     */
    private Long channelId;

    /**
     * 通道编码，优先级比channelId 低
     * 如果 channel id code 都为null，则使用默认的通道
     */
    private String channelCode;
}
