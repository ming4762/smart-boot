package com.smart.module.api.message.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "短信发送参数")
public class RemoteSmsSendParameter implements Serializable {

    /**
     * 手机号码列表
     */
    @Schema(description = "手机号码列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> phoneNumberList;

    /**
     * 短信签名
     */
    @Schema(description = "短信签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signName;

    /**
     * 短信模板
     */
    @Schema(description = "短信模板", requiredMode = Schema.RequiredMode.REQUIRED)
    private String template;

    /**
     * 模板参数
     */
    @Schema(description = "模板参数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LinkedHashMap<String, String> templateParameter;

    /**
     * 通道ID，优先级比channel code高
     * 如果 channel id code 都为null，则使用默认的通道
     */
    @Schema(description = "通道ID，优先级比channel code高")
    private Long channelId;

    /**
     * 通道编码，优先级比channelId 低
     * 如果 channel id code 都为null，则使用默认的通道
     */
    @Schema(description = "通道编码，优先级比channelId 低")
    private String channelCode;
}
