package com.smart.framework.extension.dingtalk.pojo.parameter.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * markdown消息参数
 * @author shizhongming
 * 2024/4/28 17:42
 * @since 3.0.0
 */
@Getter
@RequiredArgsConstructor
public class MarkdownMessageParameter extends AbstractMessageParameter {

    @Serial
    private static final long serialVersionUID = 738593196758949989L;

    private final String title;

    private final String text;
}
