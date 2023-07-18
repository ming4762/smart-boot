package com.smart.message.manager.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 10：未发布，20：已发布，30已撤销
 * @author zhongming4762
 * 2023/7/6 17:10
 */
@Getter
public enum MessageSendStatusEnum implements IEnum<String> {

    /**
     * 10：未发布，20：已发布，30已撤销
     */
    NO_SEND("10", "未发布"),
    SEND("20", "已发布"),
    CANCEL("30", "已取消")

    ;

    private final String value;

    private final String remark;

    MessageSendStatusEnum(String value, String remark) {
        this.value = value;
        this.remark = remark;
    }
}
