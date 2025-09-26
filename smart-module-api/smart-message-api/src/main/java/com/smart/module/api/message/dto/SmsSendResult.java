package com.smart.module.api.message.dto;

import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import lombok.*;

/**
 * 短信发送结果
 * @author shizhongming
 * 2025/9/26 14:03
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsSendResult extends MessageSendResult {

    /**
     * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
    private String requestId;

    /**
     * json格式的响应数据
     */
    private String responseData;

    private Long channelId;

    private String channelCode;

    private SmartSmsChannelEnum channelType;
}
