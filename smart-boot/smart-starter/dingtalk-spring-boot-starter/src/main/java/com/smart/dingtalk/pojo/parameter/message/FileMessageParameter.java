package com.smart.dingtalk.pojo.parameter.message;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;

/**
 * 文件消息参数
 * @author shizhongming
 * 2024/4/28 17:42
 * @since 3.0.0
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FileMessageParameter extends AbstractMessageParameter {

    @Serial
    private static final long serialVersionUID = -8532492996128484692L;

    private final String mediaId;
}
