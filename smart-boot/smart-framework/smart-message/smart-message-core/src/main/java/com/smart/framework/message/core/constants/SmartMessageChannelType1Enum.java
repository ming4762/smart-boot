package com.smart.framework.message.core.constants;

import com.smart.framework.commons.core.constants.LabelValueEnum;
import lombok.Getter;

/**
 * @author shizhongming
 * 2024/5/17 17:16
 * @since 3.0.0
 */
@Getter
public enum SmartMessageChannelType1Enum implements LabelValueEnum {

    /**
     * 一级通道类型，SYSTEM系统消息、SMS短信、EMAIL邮件、WECHAT微信、DINGDING钉钉、WEB_SOCKET
     */
    SYSTEM("系统消息", true),
    SMS("短信", false),
    EMAIL("邮件", false),
    WECHAT("微信", false),
    DINGTALK("钉钉", false),
    WEB_SOCKET("web socket", true)

    ;

    private final String remark;

    private final Boolean builtIn;

    SmartMessageChannelType1Enum(String remark, Boolean builtIn) {
        this.remark = remark;
        this.builtIn = builtIn;
    }

    /**
     * 获取value
     *
     * @return value
     */
    @Override
    public String getValue() {
        return this.name();
    }

    /**
     * 获取label
     *
     * @return label
     */
    @Override
    public String getLabel() {
        return this.remark;
    }
}
