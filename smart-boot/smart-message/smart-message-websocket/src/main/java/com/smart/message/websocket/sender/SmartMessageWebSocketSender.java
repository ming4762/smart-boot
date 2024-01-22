package com.smart.message.websocket.sender;

import com.message.core.service.SmartMessageSender;
import com.smart.message.websocket.server.WebSocket;
import com.smart.module.api.message.constants.MessageChannelEnum;
import com.smart.module.api.message.dto.MessageSendDTO;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;

/**
 * WebSocket消息发送器
 * @author shizhongming
 * 2023/1/21 20:11
 * @since 3.0.0
 */
public class SmartMessageWebSocketSender implements SmartMessageSender {


    private final WebSocket webSocket;

    public SmartMessageWebSocketSender(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    /**
     * 获取支持的通道信息
     *
     * @return 支持的消息通道
     */
    @Override
    public MessageChannelEnum supportChannel() {
        return MessageChannelEnum.WEB_SOCKET;
    }

    /**
     * 发送消息
     *
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    @Override
    public MessageSendDTO send(RemoteMessageSendParameter parameter) {
        parameter.getToUserIds().forEach(userId -> webSocket.pushMessage(userId, parameter.getContent()));
        return new MessageSendDTO();
    }
}
