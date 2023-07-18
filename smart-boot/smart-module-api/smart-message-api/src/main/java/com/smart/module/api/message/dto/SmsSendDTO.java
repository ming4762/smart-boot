package com.smart.module.api.message.dto;

import com.smart.module.api.message.constants.SmartSmsChannelEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/5/25 14:26
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendDTO implements Serializable {

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
