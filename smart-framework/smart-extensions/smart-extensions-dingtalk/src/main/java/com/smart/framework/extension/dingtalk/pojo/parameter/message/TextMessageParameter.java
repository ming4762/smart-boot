package com.smart.framework.extension.dingtalk.pojo.parameter.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * @author shizhongming
 * 2024/4/28 17:40
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public class TextMessageParameter extends AbstractMessageParameter {
    @Serial
    private static final long serialVersionUID = -8098042658186157742L;

    private final String content;
}
