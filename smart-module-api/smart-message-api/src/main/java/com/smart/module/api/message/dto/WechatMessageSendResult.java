package com.smart.module.api.message.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信消息发送结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-03 17:20
 * @since 5.0.0
 */
@Getter
@Setter
public class WechatMessageSendResult extends MessageSendResult {

    private String messageId;
}
