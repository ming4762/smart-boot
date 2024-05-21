package com.message.core.constants;

import com.smart.commons.core.constants.LabelValueEnum;
import lombok.Getter;

/**
 * 二级消息通道
 * @author shizhongming
 * 2024/5/17 17:19
 * @since 3.0.0
 */
@Getter
public enum SmartMessageChannelType2Enum implements LabelValueEnum {

    /**
     * 二级通道类型，例如阿里云短信、腾讯云短信
     */
    SMS_ALIYUN("阿里云短信", SmartMessageChannelType1Enum.SMS),
    SMS_TENCENT("腾讯云短信", SmartMessageChannelType1Enum.SMS),
    DINGTALK_WORK_NOTICE("钉钉工作通知", SmartMessageChannelType1Enum.DINGTALK),
    ;

    private final String remark;

    private final SmartMessageChannelType1Enum smartMessageType1;

    SmartMessageChannelType2Enum(String remark, SmartMessageChannelType1Enum smartMessageType1) {
        this.remark = remark;
        this.smartMessageType1 = smartMessageType1;
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
