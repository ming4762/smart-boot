package com.smart.framework.extension.wechat.constants;

/**
 * 微信模块常量
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026/2/28 23:47
 * @since 5.0.0
 */
public interface WechatConstants {

    /**
     * stream绑定名称，用于发送微信消息事件到MQ
     */
    String WECHAT_MESSAGE_TOPIC = "stream-wechat-message";
    String STREAM_WECHAT_MESSAGE_BINDING = "stream-wechat-message-binding";
}
