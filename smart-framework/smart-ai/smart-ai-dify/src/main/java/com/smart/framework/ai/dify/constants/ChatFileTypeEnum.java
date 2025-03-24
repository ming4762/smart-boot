package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * @author shizhongming
 * 2025/2/8 19:35
 * @since 5.0.0
 */
@Getter
public enum ChatFileTypeEnum implements EnumValue {

    /**
     * 图片类型
     */
    IMAGE("image"),
    ;

    private final String value;

    ChatFileTypeEnum(String value) {
        this.value = value;
    }
}
