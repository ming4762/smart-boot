package com.smart.dingtalk.pojo.parameter.message;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;

/**
 * 连接消息参数
 * @author shizhongming
 * 2024/4/28 17:42
 * @since 3.0.0
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class LinkMessageParameter extends AbstractMessageParameter {

    @Serial
    private static final long serialVersionUID = 7964656988155140500L;

    @NotNull(message = "消息点击链接地址不能为空")
    private final String messageUrl;

    @NotNull(message = "图片地址不能为空")
    private final String picUrl;

    @NotNull(message = "消息标题不能为空")
    private final String title;

    @NotNull(message = "消息描述不能为空")
    private final String text;
}
