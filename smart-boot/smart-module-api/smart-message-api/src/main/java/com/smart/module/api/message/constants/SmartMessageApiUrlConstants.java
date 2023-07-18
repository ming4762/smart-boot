package com.smart.module.api.message.constants;

/**
 * 消息模块远程调用地址
 * @author zhongming4762
 * 2023/6/6
 */
public interface SmartMessageApiUrlConstants {

    /**
     * 发送短信接口
     */
    String SMS_SEND = "/remote/message/sms/send";

    /**
     * 发送消息接口
     */
    String SEND = "/remote/message/send";
}
