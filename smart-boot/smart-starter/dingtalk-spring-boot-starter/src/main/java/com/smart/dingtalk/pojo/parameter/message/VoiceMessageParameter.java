package com.smart.dingtalk.pojo.parameter.message;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * 语音消息
 * @author shizhongming
 * 2024/4/28 17:42
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public class VoiceMessageParameter extends AbstractMessageParameter {

    @Serial
    private static final long serialVersionUID = 2249536314954635660L;

    private final String mediaId;

    @JsonSerialize(using = ToStringSerializer.class)
    private final Long duration;
}
