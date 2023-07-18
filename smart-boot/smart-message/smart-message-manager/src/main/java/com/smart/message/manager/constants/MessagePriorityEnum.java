package com.smart.message.manager.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 优先级，L低，M中，H高
 * @author zhongming4762
 * 2023/7/6 17:06
 */
@Getter
public enum MessagePriorityEnum implements IEnum<String> {

    /**
     * 优先级，L低，M中，H高
     */
    LOW("L"),
    MIDDLE("M"),
    HIGH("H")
    ;

    private final String value;

    MessagePriorityEnum(String value) {
        this.value = value;
    }
}
