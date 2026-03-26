package com.smart.framework.message.core.service;

import com.smart.framework.message.core.constants.SmartMessageChannelType1Enum;
import com.smart.framework.message.core.constants.SmartMessageChannelType2Enum;
import com.smart.framework.message.core.pojo.dto.SmartMessageToUserDTO;
import com.smart.module.api.message.constants.SmartMessageContentTypeEnum;
import com.smart.module.api.message.dto.MessageSendResult;
import com.smart.module.api.message.parameter.RemoteMessageSendParameter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * 消息发送接口
 * @author zhongming4762
 * 2023/7/14 18:02
 */
public interface SmartMessageSender {

    /**
     * 获取支持的一级通道信息
     * @return 支持的消息通道
     */
    @NonNull
    SmartMessageChannelType1Enum supportChannel1();

    /**
     * 获取支持的二级通道信息
     * @return 支持的消息通道
     */
    @Nullable
    default SmartMessageChannelType2Enum supportChannel2() {
        return null;
    }

    /**
     * 发送消息
     * @param channelProperties 通道参数
     * @param toUserList 用户列表
     * @param parameter 消息发送参数
     * @return 消息发送结果
     */
    MessageSendResult send(@Nullable String channelProperties, List<SmartMessageToUserDTO> toUserList, RemoteMessageSendParameter parameter);

    /**
     * 是否是HTML消息
     * @param parameter 消息发送参数
     * @return 是否是HTML消息
     */
    default boolean isHtml(RemoteMessageSendParameter parameter) {
        return SmartMessageContentTypeEnum.HTML.equals(parameter.getContentType());
    }

    /**
     * 是否是Markdown消息
     * @param parameter 消息发送参数
     * @return 是否是Markdown消息
     */
    default boolean isMarkdown(RemoteMessageSendParameter parameter) {
        return SmartMessageContentTypeEnum.MARKDOWN.equals(parameter.getContentType());
    }
}
