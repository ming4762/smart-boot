package com.smart.message.manager.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @author zhongming4762
 * 2023/7/6 17:04
 */
@Getter
public enum MessageTypeEnum implements IEnum<String> {

    /**
     * 消息类型，10：通知公告，20：系统消息
     */
    ANNOUNCEMENT("10", "通知公告"),
    SYSTEM_MESSAGE("20", "系统消息")
    ;

    private final String value;

    private final String remark;

    MessageTypeEnum(String value, String remark) {
        this.value = value;
        this.remark = remark;
    }

}
